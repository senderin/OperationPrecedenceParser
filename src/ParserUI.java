import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserUI {
    private JTextField expressionTextField;
    private JButton enterExpressionButton;
    private JTextField resultTextField;
    private JButton resultButton;
    private JTable table1;
    private JPanel rootPanel;
    private JLabel infoLabel;

    private Parser parser;

    public ParserUI() {
        parser = new Parser();
        resultButton.setEnabled(false);

        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = parser.GetResult(GetVariableValuePairs());
                resultTextField.setText(String.valueOf(result));
            }
        });

        enterExpressionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(parser.Parse(expressionTextField.getText())) {
                    LoadTable(parser.GetVariables());
                    infoLabel.setText(parser.outputQueue.toString());
                    resultButton.setEnabled(true);
                }
                else
                    infoLabel.setText("Expression is invalid.");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Operation Precedence Parser");
        frame.setContentPane(new ParserUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void LoadTable(List<String> variableList) {
        String[] columns = {"Variable","Value"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){

            @Override
            public boolean isCellEditable(int row, int column)
            {
                // variable column not editable
                return column != 0;
            }
        };

        table1.setModel(model);

        for(String variable : variableList) {
            model.addRow(new String[]{variable, "0"});
        }
    }

    private Map<String, Integer> GetVariableValuePairs() {
        Map<String, Integer> list = new HashMap<String, Integer>();
        for (int j = 0; j<table1.getRowCount(); j++) {
            String variable = table1.getModel().getValueAt(j, 0).toString();
            int value = Integer.parseInt(table1.getModel().getValueAt(j, 1).toString());
            list.put(variable, value);
        }
        return list;
    }

}
