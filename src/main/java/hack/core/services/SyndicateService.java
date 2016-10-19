package hack.core.services;

import java.util.Date;

import hack.core.dao.SyndicateDAO;
import hack.core.dao.TopicDAO;
import hack.core.dto.APIResultDTO;
import hack.core.dto.APIResultType;
import hack.core.models.Player;
import hack.core.models.Syndicate;
import hack.core.models.Topic;
import hack.core.models.TopicPost;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyndicateService {

	@Autowired
	private SyndicateDAO syndicateDAO;
	@Autowired
	private TopicDAO topicsDAO;
	@Autowired
	private PlayerService playerService;

	public APIResultDTO createSyndicate(Player player, String name) {

		// Validate name
		if (name.isEmpty()) {
			return new APIResultDTO(APIResultType.ERROR, "The name for the syndicate shoud be set");
		}

		// Create nameShort
		String id = trimNameShort(name);

		// Validate name doesn't already exist
		if (syndicateDAO.getSyndicateById(id) != null) {
			return new APIResultDTO(APIResultType.ERROR, "A syndicate with this short name already exists");
		}

		// Validate that user is not already in a syndicate
		if (player.getSyndicateId() != null) {
			return new APIResultDTO(APIResultType.ERROR, "You cannot start a new alliance if you are already in one");
		}

		// Validate enough money
		long requiredMoney = 1000000L;
		if (player.getMoney() < requiredMoney) {
			return new APIResultDTO(APIResultType.ERROR, "You need £" + requiredMoney + " to create an alliance");
		}

		// Create new syndicate and add user to syndicate
		Syndicate syndicate = new Syndicate();
		syndicate.setId(id);
		syndicate.setName(name);
		syndicate.getAdmins().add(player.getId());
		syndicate.getPlayers().add(player.getId());

		Topic topicWelcome = createNewTopic("Welcome", syndicate.getId());
		topicWelcome.getPosts().add(createNewPost(player.getName(), player.getId(), "Welcome to syndicate!"));
		topicWelcome.setLocked(true);
		topicsDAO.saveTopic(topicWelcome);
		syndicate.getTopics().add(topicWelcome.toTopicShort());

		Topic topicForum = createNewTopic("Forum", syndicate.getId());
		topicForum.getPosts().add(createNewPost(player.getName(), player.getId(), "Discuss..."));
		topicsDAO.saveTopic(topicForum);
		syndicate.getTopics().add(topicForum.toTopicShort());

		syndicateDAO.saveSyndicate(syndicate);

		// Reduce money
		player.setMoney(player.getMoney() - requiredMoney);
		playerService.save(player);

		// Assign syndicate to player
		playerService.updatePlayerAndLocationsToSyndicate(player.getId(), id, name);

		String message = "Syndicate created: " + id;
		return new APIResultDTO(APIResultType.SUCCESS, message);
	}

	private TopicPost createNewPost(String playerName, ObjectId playerId, String content) {
		TopicPost post = new TopicPost();
		post.setPlayerName(playerName);
		post.setPlayerId(playerId);
		post.setContent(content);
		return post;
	}

	private Topic createNewTopic(String title, String syndicateId) {
		Topic topic = new Topic();
		topic.setId(new ObjectId());
		topic.setLastAmended(new Date());
		topic.setLocked(false);
		topic.setSyndicateId(syndicateId);
		topic.setTitle(title);
		return topic;
	}

	private String trimNameShort(String nameShort) {
		nameShort = nameShort.replaceAll("[^A-Za-z]", "");
		nameShort = nameShort.substring(0, Math.min(nameShort.length(), 10));
		nameShort = nameShort.toLowerCase();
		return nameShort;
	}

	public void save(Syndicate syndicate) {
		syndicateDAO.saveSyndicate(syndicate);
	}

	public void save(Topic topic) {
		topicsDAO.saveTopic(topic);
	}

	public APIResultDTO leaveSyndicate(Player authorisingPlayer, String leavingPlayerId) {

		// Validate player is in the syndicate
		if (authorisingPlayer.getSyndicateId() == null) {
			return new APIResultDTO(APIResultType.ERROR, "You are not in a syndicate");
		}
		Syndicate syndicate = syndicateDAO.getSyndicateById(authorisingPlayer.getSyndicateId());

		boolean isAuthorisingAdmin = syndicate.getAdmins().contains(authorisingPlayer.getId());
		ObjectId leavingPlayerObjectId = new ObjectId(leavingPlayerId);

		// Validate that player is either an admin or is leaving themselves
		if (!isAuthorisingAdmin && !authorisingPlayer.getId().equals(leavingPlayerObjectId)) {
			return new APIResultDTO(APIResultType.ERROR,
					"You have to be an admin to kick anyone out of a syndicate or you can only leave yourself");
		}

		Player leavingPlayer = isSamePlayer(authorisingPlayer, leavingPlayerObjectId);
		// Validate that leavingPlayer exists
		if (leavingPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "The player you are trying to kick doesn't appear to exist");
		}

		boolean isLeavingAdmin = syndicate.getAdmins().contains(leavingPlayer.getId());

		// Validate that player is not the last admin
		if (isLeavingAdmin && syndicate.getAdmins().size() == 1) {
			return new APIResultDTO(APIResultType.ERROR, leavingPlayer.getName()
					+ " is the last admin, the syndicate must be disbanded instead");
		}

		syndicate.getPlayers().remove(leavingPlayer.getId());
		if (isLeavingAdmin) {
			syndicate.getAdmins().remove(leavingPlayer.getId());
		}
		syndicateDAO.saveSyndicate(syndicate);

		playerService.removePlayerAndLocationsFromSyndicate(leavingPlayer.getId());

		return new APIResultDTO(APIResultType.SUCCESS, leavingPlayer.getSyndicateName() + " left the syndicate");
	}

	private Player isSamePlayer(Player player, ObjectId playerId) {
		if (player.getId().equals(playerId)) {
			return player;
		} else {
			return playerService.getPlayerById(playerId);
		}
	}

	public Syndicate getSyndicateById(String id) {
		return syndicateDAO.getSyndicateById(id);
	}

	public boolean isPlayerInSyndicate(Player player, Syndicate syndicate) {
		return syndicate.getPlayers().contains(player.getId());
	}

	public boolean isAdminInSyndicate(Player player, Syndicate syndicate) {
		return syndicate.getAdmins().contains(player.getId());
	}

	public APIResultDTO syndicateJoinRequest(Player player, String syndicateId) {

		// Validate player is already in a syndicate
		if (player.getSyndicateId() != null) {
			return new APIResultDTO(APIResultType.ERROR, "You are already in a syndicate: " + player.getSyndicateName()
					+ ". You must leave there first");
		}

		Syndicate syndicate = syndicateDAO.getSyndicateById(syndicateId);

		// Validate syndicate exists
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "The syndicate doesn't appear to exist: " + syndicateId);
		}

		// Validate that the player hasn't already applied
		if (syndicate.getApplicants().contains(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You have already applied to join " + syndicate.getName());
		}

		syndicate.getApplicants().add(player.getId());
		syndicateDAO.saveSyndicate(syndicate);

		return new APIResultDTO(APIResultType.SUCCESS, "You have applied to join " + syndicate.getName());
	}

	public APIResultDTO syndicateJoinApproval(Player approvingPlayer, String applyingPlayerId, boolean approve) {

		ObjectId objectId = new ObjectId(applyingPlayerId);

		Player applyingPlayer = playerService.getPlayerById(objectId);

		// Validate applying player exists
		if (applyingPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Player does not exist: " + applyingPlayerId);
		}

		// Validate applying player is not already in a syndicate
		if (applyingPlayer.getSyndicateId() != null) {
			return new APIResultDTO(APIResultType.ERROR, "Player is already in a syndicate: " + applyingPlayer.getSyndicateName());
		}

		// Validate approving player is in syndicate
		Syndicate syndicate = getSyndicateById(approvingPlayer.getSyndicateId());
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "Approving player is not in a syndicate");
		}

		// Validate approving player is an admin
		if (!syndicate.getAdmins().contains(approvingPlayer.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "Approving player is not an admin in the syndicate: " + syndicate.getName());
		}

		// Approve / reject application
		String message = "";
		if (approve) {
			syndicate.getApplicants().remove(applyingPlayer.getId());
			syndicate.getPlayers().add(applyingPlayer.getId());
			applyingPlayer.setSyndicateId(syndicate.getId());
			applyingPlayer.setSyndicateName(syndicate.getName());
			playerService.save(applyingPlayer);
			message = applyingPlayer.getName() + " has been approved to join the syndicate: " + syndicate.getName();
		} else {
			syndicate.getApplicants().remove(applyingPlayer.getId());
			message = applyingPlayer.getName() + " has been rejected to join the syndicate: " + syndicate.getName();
		}
		syndicateDAO.saveSyndicate(syndicate);

		return new APIResultDTO(APIResultType.SUCCESS, message);
	}

	public APIResultDTO syndicateSetAdmin(Player approvingPlayer, String applyingPlayerId, boolean setAsAdmin) {

		ObjectId objectId = new ObjectId(applyingPlayerId);

		Player applyingPlayer = playerService.getPlayerById(objectId);

		// Validate applying player exists
		if (applyingPlayer == null) {
			return new APIResultDTO(APIResultType.ERROR, "Player does not exist: " + applyingPlayerId);
		}

		// Validate applying player is already in the syndicate
		if (!applyingPlayer.getSyndicateId().equals(approvingPlayer.getSyndicateId())) {
			return new APIResultDTO(APIResultType.ERROR, "Player is not in a syndicate: " + approvingPlayer.getSyndicateName());
		}

		Syndicate syndicate = syndicateDAO.getSyndicateById(approvingPlayer.getSyndicateId());

		// Validate approving player is an admin
		if (!syndicate.getAdmins().contains(approvingPlayer.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "Approving player is not an admin in the syndicate: " + syndicate.getName());
		}

		// Validate that applying player is not the last admin
		boolean isAdmin = syndicate.getAdmins().contains(applyingPlayer.getId());

		// Validate that player is not the last admin
		if (isAdmin && !setAsAdmin && syndicate.getAdmins().size() == 1) {
			return new APIResultDTO(APIResultType.ERROR, applyingPlayer.getName()
					+ " is the last admin, the syndicate must be disbanded instead");
		}

		// Approve / reject application
		String message = "";
		if (setAsAdmin) {
			if (!syndicate.getAdmins().contains(applyingPlayer.getId())) {
				syndicate.getAdmins().add(applyingPlayer.getId());
			}
			message = applyingPlayer.getName() + " is now an admin of syndicate: " + syndicate.getName();
		} else {
			syndicate.getAdmins().remove(applyingPlayer.getId());
			message = applyingPlayer.getName() + " is no longer an admin of syndicate: " + syndicate.getName();
		}
		syndicateDAO.saveSyndicate(syndicate);

		return new APIResultDTO(APIResultType.SUCCESS, message);
	}

	public APIResultDTO syndicateDisband(Player player, String disband) {
		// Validate approving using is part of syndicate
		if (player.getSyndicateId() == null) {
			return new APIResultDTO(APIResultType.ERROR, "You are not part of a syndicate therefore you cannot disband one");
		}

		// Validate syndicate exists
		Syndicate syndicate = syndicateDAO.getSyndicateById(player.getSyndicateId());
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "Syndicate does not exist");
		}

		// Validate approving user is admin
		if (!syndicate.getAdmins().contains(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You are not an admin of this syndicate");
		}

		if (!disband.equals("disband")) {
			return new APIResultDTO(APIResultType.WARNING, "If you want to disband this syndicate, type 'disband' in the disband box");
		} else {
			// Remove all relations to players and locations
			for (ObjectId playerId : syndicate.getPlayers()) {
				playerService.removePlayerAndLocationsFromSyndicate(playerId);
			}
			// Remove all syndicateDAO where syndicate.id matches
			syndicateDAO.remove(syndicate.getId());
			// Remove all topicDAO where syndicate.id matches
			topicsDAO.removeAllSyndicatesPosts(syndicate.getId());
		}

		return new APIResultDTO(APIResultType.SUCCESS, "The syndicate " + syndicate.getName() + " has successfully been disbanded");
	}

	public APIResultDTO syndicateEditDescription(Player player, String description) {
		// Validate approving using is part of syndicate
		if (player.getSyndicateId() == null) {
			return new APIResultDTO(APIResultType.ERROR, "You are not part of a syndicate therefore you cannot disband one");
		}

		// Validate syndicate exists
		Syndicate syndicate = syndicateDAO.getSyndicateById(player.getSyndicateId());
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "Syndicate does not exist");
		}

		// Validate approving user is admin
		if (!syndicate.getAdmins().contains(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You are not an admin of this syndicate");
		}

		syndicate.setDescription(description);
		syndicateDAO.saveSyndicate(syndicate);

		return new APIResultDTO(APIResultType.SUCCESS, "The syndicate " + syndicate.getName() + " description has been updated");
	}

	public Topic getTopicById(ObjectId topicId) {
		return topicsDAO.getTopicById(topicId);
	}

	public APIResultDTO topicAddPost(Player player, String content, ObjectId topicId) {
		// Validate topic exists
		Topic topic = topicsDAO.getTopicById(topicId);
		if (topic == null) {
			return new APIResultDTO(APIResultType.ERROR, "Topic does not exist");
		}

		// Validate user has access to topic, is a player in the syndicate
		if (!player.getSyndicateId().equals(topic.getSyndicateId())) {
			return new APIResultDTO(APIResultType.ERROR, "You do not have access to this topic");
		}

		// Validate that the topic is not locked
		if (topic.isLocked()) {
			return new APIResultDTO(APIResultType.ERROR, "Topic is locked");
		}

		TopicPost post = new TopicPost(player.getName(), player.getId(), content);
		topic.getPosts().add(post);
		Date lastAmended = new Date();
		topic.setLastAmended(lastAmended);
		topicsDAO.saveTopic(topic);
		syndicateDAO.updateTopicShort(topic);

		return new APIResultDTO(APIResultType.SUCCESS, "Topic Updated");
	}

	public APIResultDTO topicAddTopic(Player player, String title) {
		// Validate player is in syndicate
		Syndicate syndicate = getSyndicateById(player.getSyndicateId());
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "You are not in a syndicate");
		}

		// Validate player has access to create topic
		if (!syndicate.getAdmins().contains(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You need to be an admin to create a new topic");
		}

		Topic topic = new Topic(new ObjectId(), syndicate.getId(), title, new Date(), false);
		syndicate.getTopics().add(topic.toTopicShort());
		syndicateDAO.saveSyndicate(syndicate);

		topicsDAO.saveTopic(topic);

		return new APIResultDTO(APIResultType.SUCCESS, "Topic created");
	}

	public APIResultDTO topicRemoveTopic(Player player, ObjectId topicId) {
		// Validate player is in syndicate
		Syndicate syndicate = getSyndicateById(player.getSyndicateId());
		if (syndicate == null) {
			return new APIResultDTO(APIResultType.ERROR, "You are not in a syndicate");
		}

		// Validate player has access to create topic
		if (!syndicate.getAdmins().contains(player.getId())) {
			return new APIResultDTO(APIResultType.ERROR, "You need to be an admin to create a new topic");
		}

		// Validate topic exists
		Topic topic = topicsDAO.getTopicById(topicId);
		if(topic == null) {
			return new APIResultDTO(APIResultType.ERROR, "Topic does not exist");
		}
		
		syndicate.getTopics().remove(topic.toTopicShort());
		syndicateDAO.saveSyndicate(syndicate);

		topicsDAO.removeTopic(topic);

		return new APIResultDTO(APIResultType.SUCCESS, "Topic removed");
	}
}
