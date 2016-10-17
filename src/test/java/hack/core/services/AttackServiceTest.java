package hack.core.services;

import java.util.ArrayList;
import java.util.List;

import hack.core.models.Player;
import hack.core.models.Research;
import hack.core.models.ResearchType;
import hack.core.models.Troop;
import hack.core.models.TransitTroop;
import hack.core.models.TroopType;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttackServiceTest {

	@InjectMocks
	private AttackService accountService = new AttackService();

	private static TroopType TYPE = TroopType.FIREWALL;
	private static String SOURCE = "111.111.111.111";
	private static String TARGET = "222.222.222.222";

	@Test
	public void basicScoreEqual1s() {
		calculateAttackVictory("Equal 1s", 1, 1, 1, 1, 1, 1);
	}
	@Test
	public void basicScoreEqual10s() {
		calculateAttackVictory("Equal 10s", 10, 10, 10, 10, 10, 10);
	}
	@Test
	public void basicScoreEqual50s() {
		calculateAttackVictory("Equal 20s", 20, 20, 20, 20, 20, 20);
	}
	@Test
	public void basicScoreEqual100s() {
		calculateAttackVictory("Equal 100s", 100, 100, 100, 100, 100, 100);
	}
	@Test
	public void basicScoreEqual1000s() {
		calculateAttackVictory("Equal 1000s", 1000, 1000, 1000, 1000, 1000, 1000);
	}

	@Test
	public void basicScoreHighAttackBoost10s() {
		calculateAttackVictory("High Attack Boost 10s", 20, 10, 10, 10, 10, 10);
	}
	@Test
	public void basicScoreHighAttackBoost1000s() {
		calculateAttackVictory("High Attack Boost 1000s", 2000, 1000, 1000, 1000, 1000, 1000);
	}

	@Test
	public void basicScoreHighDefenseBase10s() {
		calculateAttackVictory("High Defense Base 10s", 10, 10, 20, 10, 10, 10);
	}
	@Test
	public void basicScoreHighDefenseBase1000s() {
		calculateAttackVictory("High Defense Base 1000s", 1000, 1000, 2000, 1000, 1000, 1000);
	}

	@Test
	public void basicScoreHighDefenseBoost10s() {
		calculateAttackVictory("High Defense Boost 10s", 10, 10, 10, 20, 10, 10);
	}
	@Test
	public void basicScoreHighDefenseBoost1000s() {
		calculateAttackVictory("High Defense Boost 1000s", 1000, 1000, 1000, 2000, 1000, 1000);
	}

	@Test
	public void basicScoreHighDefenseCount10s() {
		calculateAttackVictory("High Defense Count 10s", 10, 10, 10, 10, 20, 10);
	}
	@Test
	public void basicScoreHighDefenseCount1000s() {
		calculateAttackVictory("High Defense Count 1000s", 1000, 1000, 1000, 1000, 2000, 1000);
	}
	
	@Test
	public void basicScoreHighDefenseInCount10s() {
		calculateAttackVictory("High Defense In Count 10s", 10, 10, 10, 10, 10, 20);
	}
	@Test
	public void basicScoreHighDefenseInCount1000s() {
		calculateAttackVictory("High Defense In Count 1000s", 1000, 1000, 1000, 1000, 1000, 2000);
	}	
	
	
	private boolean calculateAttackVictory(String testName, int ATTACK_BOOST, int ATTACK_TROOPS,
			int DEFENSE_BASE, int DEFENSE_BOOST, int DEFENSE_TROOPS,
			int DEFENSE_IN_TROOPS) {
		Player attackPlayer = generateAttackPlayer(ATTACK_BOOST);
		TransitTroop attackingTroop = generateTransitTroop(ATTACK_TROOPS);
		double aScore = accountService.calculateAttackScore(attackPlayer, TYPE,
				attackingTroop);

		Player defensePlayer = generateDefensePlayer(DEFENSE_BASE,
				DEFENSE_BOOST);
		Troop defendingTroop = generateTroop(DEFENSE_TROOPS);
		List<TransitTroop> defendingInTroops = generateTransitTroops(DEFENSE_IN_TROOPS);

		double dScore = accountService.calculateDefenseScore(defensePlayer,
				TYPE, defendingTroop, defendingInTroops);
		boolean attackVictory = aScore > dScore;
		double factor = attackVictory ? Math.abs(aScore - dScore) / Math.abs(aScore) : Math.abs(dScore - aScore) / Math.abs(dScore);
		System.out.println(testName + " - "+ aScore + " vs " + dScore + " - "
				+ attackVictory + " - " + factor);
		return attackVictory;
	}

	private Player generateAttackPlayer(int attackBoostLevel) {
		Player player = new Player();
		player.getResearches().add(
				new Research(ResearchType.ATTACK_BONUS_FIREWALL,
						attackBoostLevel));
		return player;
	}

	private Player generateDefensePlayer(int defenseBaseLevel,
			int defenseBoostLevel) {
		Player player = new Player();
		player.getResearches().add(
				new Research(ResearchType.DEFENCE_BASE, defenseBaseLevel));
		player.getResearches().add(
				new Research(ResearchType.DEFENSE_HARDEN_FIREWALL,
						defenseBoostLevel));
		return player;
	}

	private Troop generateTroop(int noOfTroops) {
		return new Troop(TYPE, noOfTroops);
	}

	private TransitTroop generateTransitTroop(int noOfTroops) {
		return new TransitTroop(TYPE, noOfTroops, SOURCE, TARGET, null);
	}

	private List<TransitTroop> generateTransitTroops(int noOfTroops) {
		List<TransitTroop> troops = new ArrayList<TransitTroop>();
		troops.add(new TransitTroop(TYPE, noOfTroops, SOURCE, TARGET, null));
		return troops;
	}
}
