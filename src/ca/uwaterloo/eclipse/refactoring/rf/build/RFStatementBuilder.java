package ca.uwaterloo.eclipse.refactoring.rf.build;

import ca.uwaterloo.eclipse.refactoring.rf.node.*;
import ca.uwaterloo.eclipse.refactoring.rf.template.RFTemplate;
import gr.uom.java.ast.decomposition.StatementType;
import gr.uom.java.ast.decomposition.cfg.mapping.*;
import gr.uom.java.ast.decomposition.matching.ASTNodeDifference;
import gr.uom.java.ast.decomposition.matching.Difference;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import java.util.ArrayList;
import java.util.List;

public class RFStatementBuilder {

    private static RFStatementBuilder builder;

    private RFStatementBuilder() {
    }

    public static RFStatementBuilder getInstance() {
        if (builder == null) {
            builder = new RFStatementBuilder();
        }
        return builder;
    }

    public RFStatement build(CloneStructureNode root, RFTemplate template) {
        // build refactorable tree from root
        return build(root, template, null);
    }

    private RFStatement build(CloneStructureNode root, RFTemplate template, RFStatement parent) {

        // build current node
        RFStatement currentNode;
        if (parent == null) {
            currentNode = new RFStatement(template);
        } else {
            assert root.getMapping() != null;
            currentNode = buildCurrent(root.getMapping(), template);
        }

        // ignore the gap node and its children
        if (currentNode == null) {
            return null;
        }

        // build children nodes
        currentNode.setParent(parent);
        for (CloneStructureNode child: root.getChildren()) {
            RFStatement childStmt = build(child, template, currentNode);
            if (childStmt != null) {
                currentNode.addChild(childStmt);
            }
        }

        return currentNode;
    }

    public RFStatement buildCurrent(NodeMapping nodeMapping, RFTemplate template) {

        if (nodeMapping instanceof PDGNodeMapping) {
            return buildFromPDGNodeMapping(nodeMapping, template);

        } else if (nodeMapping instanceof PDGElseMapping) {
            System.out.println("PDG else mapping");
            return buildFromPDGElseMapping(nodeMapping, template);

        } else if (nodeMapping instanceof PDGNodeGap) {
            System.out.println("-----------------------------------------------------------");
            System.out.println("TO DO: PDG node gap node: ");
            System.out.println(nodeMapping.toString());
            return null;

        } else if (nodeMapping instanceof PDGElseGap) {
            System.out.println("-----------------------------------------------------------");
            System.out.println("TO DO: PDG else gap node");
            return null;

        } else {
            System.out.println("unknown nodeMapping");
            return null;
        }
    }

    private RFStatement buildFromPDGNodeMapping(NodeMapping nodeMapping, RFTemplate template) {
        List<RFNodeDifference> mapping = new ArrayList<>();
        for (ASTNodeDifference astNodeDifference : nodeMapping.getNodeDifferences()) {
            Expression expr1 = astNodeDifference.getExpression1().getExpression();
            Expression expr2 = astNodeDifference.getExpression2().getExpression();
            List<Difference> differences = astNodeDifference.getDifferences();
            mapping.add(new RFNodeDifference(expr1, expr2, differences));
        }

        Statement statement1 = nodeMapping.getNodeG1().getASTStatement();
        Statement statement2 = nodeMapping.getNodeG2().getASTStatement();

        assert nodeMapping.getNodeG1().getStatement().getType() == nodeMapping.getNodeG2().getStatement().getType();
        StatementType statementType = nodeMapping.getNodeG1().getStatement().getType();

        /*
        System.out.println("building RFStatement: ");
        System.out.println("\ttype: " + statementType);
        System.out.println("\tstatement1: " + statement1);
        System.out.println("\tstatement2: " + statement2);
        System.out.println("\tmapping: ");
        for(RFNodeDifference difference: mapping) {
            System.out.println("\t\t" + difference);
        }
        */

        return new RFStatement(statementType, statement1, statement2, mapping, template);
    }

    private RFStatement buildFromPDGElseMapping(NodeMapping nodeMapping, RFTemplate template) {
        // Else RFStatement has no statement type but parent node
        return new RFStatement(null, null, null, new ArrayList<>(), template);
    }

}
