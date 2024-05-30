
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class currencyConverter extends JFrame {
    JComboBox<String> jComboBox1, jComboBox2;
    JTextField textField;
    JLabel resultLabel;

    currencyConverter() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setBackground(Color.white);
        jPanel.setVisible(true);
        this.setTitle("CurrencyConverter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(340, 500);
        this.setVisible(true);
        this.setLocation(250, 100);
        this.add(jPanel);

        JLabel title = new JLabel("Currency Converter");
        title.setBounds(62, 20, 220, 30);
        title.setFont(new Font("", Font.CENTER_BASELINE, 20));
        jPanel.add(title);

        JLabel EnterAmount = new JLabel("Enter Amount");
        EnterAmount.setBounds(40, 100, 170, 20);
        EnterAmount.setFont(new Font("", Font.BOLD, 16));
        jPanel.add(EnterAmount);

        textField = new JTextField();
        textField.setBounds(40, 140, 263, 30);
        jPanel.add(textField);

        JLabel from = new JLabel("From");
        from.setBounds(40, 200, 100, 20);
        from.setFont(new Font("", Font.BOLD, 16));
        jPanel.add(from);

        JLabel arrow = new JLabel("---");
        arrow.setBounds(160, 200, 100, 20);
        arrow.setFont(new Font("", Font.BOLD, 16));
        jPanel.add(arrow);

        JLabel to = new JLabel("To");
        to.setBounds(230, 200, 100, 20);
        to.setFont(new Font("", Font.BOLD, 16));
        jPanel.add(to);

        String[] currencies = getAllCurrencies();
        jComboBox1 = new JComboBox<>(currencies);
        jComboBox1.setBounds(40, 240, 100, 20);
        jComboBox1.setBackground(Color.white);
        jPanel.add(jComboBox1);

        jComboBox2 = new JComboBox<>(currencies);
        jComboBox2.setBounds(200, 240, 100, 20);
        jComboBox2.setBackground(Color.white);
        jPanel.add(jComboBox2);

        JButton button = new JButton("Get Exchange Rate");
        button.setBounds(40, 330, 260, 30);
        button.setFocusable(false);
        button.setFont(new Font("", Font.BOLD, 16));
        button.setBackground(new Color(95, 95, 203));
        jPanel.add(button);

        resultLabel = new JLabel();
        resultLabel.setBounds(40, 370, 260, 20);
        resultLabel.setFont(new Font("", Font.BOLD, 16));
        jPanel.add(resultLabel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromCurrency = (String) jComboBox1.getSelectedItem();
                String toCurrency = (String) jComboBox2.getSelectedItem();
                double amount = Double.parseDouble(textField.getText());
                double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
                double convertedAmount = amount * exchangeRate;
                resultLabel.setText("Converted Amount: " + convertedAmount);
            }
        });
    }

    private String[] getAllCurrencies() {
        try {
            URL url = new URL("https://api.exchangerate-api.com/v4/latest/USD");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject rates = jsonObject.getJSONObject("rates");
            String[] currencies = new String[rates.length() + 1];
            currencies[0] = "USD"; // Base currency
            int index = 1;
            for (String currency : rates.keySet()) {
                currencies[index++] = currency;
            }
            return currencies;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"USD"}; // Return default currency
        }
    }

    private double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            URL url = new URL("https://api.exchangerate-api.com/v4/latest/" + fromCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject rates = jsonObject.getJSONObject("rates");
            return rates.getDouble(toCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Return 0.0 if there's any error
        }
    }

    public static void main(String args[]) {
        new currencyConverter();
    }
}
