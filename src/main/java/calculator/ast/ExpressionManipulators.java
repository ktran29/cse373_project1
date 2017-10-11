package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;
import datastructures.concrete.DoubleLinkedList;

/**
 * All of the static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Takes the given AstNode node and attempts to convert it into a double.
     *
     * Returns a number AstNode containing the computed double.
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode toDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the TODOs in the 'toDoubleHelper' method.
        return new AstNode(toDoubleHelper(env.getVariables(), node));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                // If the expression contains an undefined variable, we give up.
                throw new EvaluationError("Undefined variable: " + node.getName());
            } else {
            		AstNode variable = variables.get(node.getName());
            		if (variable.isNumber()) {
            			return variable.getNumericValue();
            		} else {
            			return toDoubleHelper(variables, variable);
            		}
            }
        } else {
            String name = node.getName();

            	if(name.equals("toDouble")) {
            		return toDoubleHelper(variables, node.getChildren().get(0));
            	}
            
            if (name.equals("+")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) + toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("-")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) - toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("*")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) * toDoubleHelper(variables,
                node.getChildren().get(1));
            } else if (name.equals("/")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) / toDoubleHelper(variables,
                node.getChildren().get(1));
            } else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, node.getChildren().get(0)), toDoubleHelper(variables, node.getChildren().get(1)));
            } else if (name.equals("negate")) {
                return -(toDoubleHelper(variables, node.getChildren().get(0)));
            } else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, node.getChildren().get(0)));
            } else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, node.getChildren().get(0)));
            } else {
                throw new EvaluationError("Unknown operation: " + name);
            }
        }
    }

    public static AstNode simplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "toDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "toDouble" method in some way
    	
    		
    	
    		return simplifyHelper(env.getVariables(), node.getChildren().get(0));
    }
    
    private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
		if(node.isOperation()) {
			String operation = node.getName();
			
			AstNode leftChild = simplifyHelper(variables, node.getChildren().get(0));
			AstNode rightChild = new AstNode(0);
			if(node.getChildren().size() > 1) {
				rightChild = simplifyHelper(variables, node.getChildren().get(1));
			}
			
			switch (operation) {
				case "+": {
					
					if (leftChild.isNumber() && rightChild.isNumber()) {
						return new AstNode((int) (leftChild.getNumericValue() + rightChild.getNumericValue()));
					} else {
						node.getChildren().set(0, leftChild);
						node.getChildren().set(1, rightChild);
						return node;
					}
					
				}
				case "-": {
					
					if (leftChild.isNumber() && rightChild.isNumber()) {
						return new AstNode((int) (leftChild.getNumericValue() - rightChild.getNumericValue()));
					} else {
						node.getChildren().set(0, leftChild);
						node.getChildren().set(1, rightChild);
						return node;
					}
				}
				
				case "*": {
					
					if (leftChild.isNumber() && rightChild.isNumber()) {
						return new AstNode((int) (leftChild.getNumericValue() * rightChild.getNumericValue()));
					} else {
						node.getChildren().set(0, leftChild);
						node.getChildren().set(1, rightChild);
						return node;
					}
				}
					
				case "^": {
					if (leftChild.isNumber() && rightChild.isNumber()) {
						return new AstNode((int) Math.pow(leftChild.getNumericValue(), rightChild.getNumericValue()));
					} else {
						node.getChildren().set(0, leftChild);
						node.getChildren().set(1, rightChild);
						return node;
					}
				}
					
				case "negate": {
					if (leftChild.isNumber()) {
						return new AstNode(-1 * (int) leftChild.getNumericValue());
					}
				}
				
				default: {
					node.getChildren().set(0, leftChild);
					if(node.getChildren().size() > 1) {
						node.getChildren().set(1, rightChild);
					}
					return node;
				}
			
			}
			
		} else {
			if (node.isNumber()) {
				return new AstNode(node.getNumericValue());
			} else {
				if (variables.containsKey(node.getName())) {
					if (!variables.get(node.getName()).isNumber()) {
						return variables.get(node.getName());
					} else {
						return new AstNode(variables.get(node.getName()).getNumericValue());
					}
				} else {
					return new AstNode(node.getName());
				}
			}
		}
    }
    


    /**
     * Expected signature of plot:
     *
     * >>> plot(exprToPlot, var, varMin, varMax, step)
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This command will plot the equation "3 * x", varying "x" from 2 to 5 in 0.5
     * increments. In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
    	double step = node.getChildren().get(4).getNumericValue();
    	if (step <= 0) {
    		throw new EvaluationError("Step value must be positive");
    	}
    	double varMin = node.getChildren().get(2).getNumericValue();
    	double varMax = node.getChildren().get(3).getNumericValue();
    	if (varMin > varMax) {
    		throw new EvaluationError("varMin must be smaller or equal to varMax");
    	}
    	int totalPoints = (int) (((varMax - varMin) / step) + 1);
    	String var = node.getChildren().get(2).getName();
    	if (!env.getVariables().containsKey(var)) {
    		throw new EvaluationError("Variable to plot already defined in expression");
    	}
    	DoubleLinkedList<Double> xValues = new DoubleLinkedList<Double>();
    	DoubleLinkedList<Double> yValues = new DoubleLinkedList<Double>();
    	for (int i = 0; i < totalPoints; i++) {
    		double currentX = (varMin + (step*i));
    		xValues.add(currentX);
    		AstNode xNode = new AstNode(currentX);
    		env.getVariables().put(var, xNode);
    		AstNode yNode = toDouble(env, node);
    		double currentY = yNode.getNumericValue();
    		yValues.add(currentY);
    	}
    	env.getVariables().remove(var);
    	
    	env.getImageDrawer().drawScatterPlot("", "X", "Y", xValues, yValues);

        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        return new AstNode(1);
    }
}
