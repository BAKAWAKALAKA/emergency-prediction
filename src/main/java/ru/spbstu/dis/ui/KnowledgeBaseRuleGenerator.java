package ru.spbstu.dis.ui;

import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Bisector;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;

/**
 * Created by a.fedorov on 08.04.2016.
 */
public class KnowledgeBaseRuleGenerator {
	public static String actionName = "ACTION";

	static String highLevelName = "HIGH";

	public static String emergencyStopAction = "EMERGENCY_STOP";

	static String normalLevelName = "NORMAL";

	static final String growthName = "GROWTH";

	static final String closenessName = "CLOSENESS";

	static final String riskName = "RISK";

	public static String userDecisionAction = "USER_DECISION";

	public static String lowLevelName = "LOW";

	static String doNothingAction = "DO_NOTHING";

	public static final Triangle LOW = new Triangle(lowLevelName, 0.000, 0.250, 0.500);

	public static final Triangle NORMAL = new Triangle(normalLevelName, 0.250, 0.500, 0.750);

	public static final Triangle HIGHT = new Triangle(highLevelName, 0.500, 0.750, 10.000);

	public static InputVariable tGrowth;

	public static OutputVariable action;

	public static Engine engine = new Engine();
	public static InputVariable tCloseness;
	public static InputVariable tankOverflowRisk;

	public static void generateRulesForOverflowOfTank() {
		engine.setName("EmergencyPredictor");
		tGrowth = new InputVariable(growthName);
		generateTriangularTerm(tGrowth);
		engine.addInputVariable(tGrowth);

		tCloseness = new InputVariable(closenessName);
		generateTriangularTerm(tCloseness);
		engine.addInputVariable(tCloseness);

		tankOverflowRisk = new InputVariable(riskName);
		generateTriangularTerm(tankOverflowRisk);
		engine.addInputVariable(tankOverflowRisk);

    action = new OutputVariable();
    action.setName("ACTION");
    action.setRange(0.000, 1.000);
    action.setDefaultValue(Double.NaN);
    action.addTerm(new Triangle("DO_NOTHING", 0.000, 0.250, 0.500));
    action.addTerm(new Triangle("USER_DECISION", 0.250, 0.500, 0.750));
    action.addTerm(new Triangle("EMERGENCY_STOP", 0.500, 0.750, 1.000));
    engine.addOutputVariable(action);

		RuleBlock ruleBlock = new RuleBlock("firstBlock", new Minimum(), new Maximum(), new Minimum());

		ruleBlock
				.addRule(
						Rule.parse(
								"if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
										+ highLevelName + " then " + actionName + " is " + emergencyStopAction,
								engine));

		ruleBlock
				.addRule(
						Rule.parse(
								"if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
										+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
								engine));

		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
				+ lowLevelName + " then " + actionName + " is " + userDecisionAction, engine));
		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
				+ highLevelName + " then " + actionName + " is " + userDecisionAction, engine));

		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
				+ normalLevelName + " then " + actionName + " is " + doNothingAction, engine));
		ruleBlock
				.addRule(
						Rule.parse(
								"if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
										+ lowLevelName + " then" + " " + actionName + " is " + doNothingAction,
								engine));
		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
				+ highLevelName + " then " + actionName + " is " + userDecisionAction, engine));
		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
				+ normalLevelName + " then " + actionName + " is " + doNothingAction, engine));
		ruleBlock.addRule(Rule.parse("if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
				+ lowLevelName + " then " + actionName + " is " + doNothingAction, engine));

		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
				+ normalLevelName + " and " + growthName + " is " + highLevelName + " " + "then " + actionName + " is "
				+ emergencyStopAction, engine));

		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
				+ highLevelName + " then " + actionName + " is " + userDecisionAction, engine));
		ruleBlock
				.addRule(
						Rule.parse(
								"if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
										+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
								engine));
		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
				+ lowLevelName + " then " + actionName + " is " + emergencyStopAction, engine));
		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
				+ highLevelName + " then " + actionName + " is " + doNothingAction, engine));
		ruleBlock
				.addRule(
						Rule.parse(
								"if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
										+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
								engine));
		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
				+ lowLevelName + " then " + actionName + " is " + userDecisionAction, engine));
		ruleBlock.addRule(Rule.parse("if " + riskName + " is " + lowLevelName + " and " + growthName + " is "
				+ highLevelName + " then " + actionName + " is " + userDecisionAction, engine));
		engine.addRuleBlock(ruleBlock);


		engine.configure(new AlgebraicProduct(), new Maximum(), new Minimum(), new Maximum(), new Bisector());
	}

	private static void generateTriangularTerm(InputVariable tankOverflowRisk) {
		tankOverflowRisk.setRange(0.000, 10.000);
		tankOverflowRisk.addTerm(LOW);
		tankOverflowRisk.addTerm(NORMAL);
		tankOverflowRisk.addTerm(HIGHT);
	}

}
