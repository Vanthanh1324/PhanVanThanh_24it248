package Data_Ki2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderManagement extends JFrame {
    private JPanel bangDieuKhien, cardPanel, nut, inputPanel;
    private CardLayout cardLayout;
    private JButton btnKhachHang, btnDonHang, btnSanPham, btnChiTietDonHang, btnThem, btnSua, btnXoa, btnIn;
    private JButton btnXemLichSu, btnTinhTong;
    private JTable tableKhachHang, tableDonHang, tableSanPham, tableChiTietDonHang;
    private Connection conn;
    private HashMap<String, ArrayList<JTextField>> inputFields;
    private String currentTable;

    public OrderManagement() {
        setTitle("Quản Lý Đơn Hàng");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectToDatabase();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel bangKhachHang = new JPanel(new BorderLayout());
        JPanel bangDonHang = new JPanel(new BorderLayout());
        JPanel bangSanPham = new JPanel(new BorderLayout());
        JPanel bangChiTietDonHang = new JPanel(new BorderLayout());

        bangKhachHang.setBorder(BorderFactory.createTitledBorder("Khách Hàng"));
        bangDonHang.setBorder(BorderFactory.createTitledBorder("Đơn Hàng"));
        bangSanPham.setBorder(BorderFactory.createTitledBorder("Sản Phẩm"));
        bangChiTietDonHang.setBorder(BorderFactory.createTitledBorder("Chi Tiết Đơn Hàng"));

        tableKhachHang = new JTable();
        tableDonHang = new JTable();
        tableSanPham = new JTable();
        tableChiTietDonHang = new JTable();

        if (conn != null) {
            loadTableData("khachhang", tableKhachHang);
            loadTableData("donhang", tableDonHang);
            loadTableData("sanpham", tableSanPham);
            loadTableData("ChiTietDonHang", tableChiTietDonHang);
        } else {
            System.err.println("Không thể kết nối đến cơ sở dữ liệu.");
        }

        bangKhachHang.add(new JScrollPane(tableKhachHang), BorderLayout.CENTER);
        bangDonHang.add(new JScrollPane(tableDonHang), BorderLayout.CENTER);
        bangSanPham.add(new JScrollPane(tableSanPham), BorderLayout.CENTER);
        bangChiTietDonHang.add(new JScrollPane(tableChiTietDonHang), BorderLayout.CENTER);

        cardPanel.add(bangKhachHang, "khachhang");
        cardPanel.add(bangDonHang, "donhang");
        cardPanel.add(bangSanPham, "sanpham");
        cardPanel.add(bangChiTietDonHang, "ChiTietDonHang");

        bangDieuKhien = new JPanel(new FlowLayout());
        nut = new JPanel(new FlowLayout());
        inputPanel = new JPanel(new GridLayout(0, 2));
        inputFields = new HashMap<>();

        btnKhachHang = new JButton("Khách Hàng");
        btnDonHang = new JButton("Đơn Hàng");
        btnSanPham = new JButton("Sản Phẩm");
        btnChiTietDonHang = new JButton("Chi Tiết Đơn Hàng");

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnIn = new JButton("In Đơn Hàng");

        btnXemLichSu = new JButton("Xem Lịch Sử");
        btnTinhTong = new JButton("Tính Tổng");

        bangDieuKhien.add(btnKhachHang);
        bangDieuKhien.add(btnDonHang);
        bangDieuKhien.add(btnSanPham);
        bangDieuKhien.add(btnChiTietDonHang);

        nut.add(btnThem);
        nut.add(btnSua);
        nut.add(btnXoa);
        nut.add(btnIn);
        nut.add(btnXemLichSu);
        nut.add(btnTinhTong);

        add(bangDieuKhien, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.EAST);
        add(nut, BorderLayout.SOUTH);

        btnKhachHang.addActionListener(e -> switchTable("khachhang"));
        btnDonHang.addActionListener(e -> switchTable("donhang"));
        btnSanPham.addActionListener(e -> switchTable("sanpham"));
        btnChiTietDonHang.addActionListener(e -> switchTable("ChiTietDonHang"));

        btnThem.addActionListener(e -> handleAdd());
        btnSua.addActionListener(e -> handleUpdate());
        btnXoa.addActionListener(e -> handleDelete());
        btnIn.addActionListener(e -> printOrder());
        btnXemLichSu.addActionListener(e -> viewOrderHistory());
        btnTinhTong.addActionListener(e -> calculateTotalAmount());

        switchTable("khachhang");
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=DAO;user=sa;password=123456789;encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(url);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            conn = null;
        }
    }

    private void loadTableData(String tableName, JTable table) {
        try {
            String query = "SELECT * FROM " + tableName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchTable(String tableName) {
        currentTable = tableName;
        cardLayout.show(cardPanel, tableName);
        generateInputFields(tableName);
    }

    private void generateInputFields(String tableName) {
        inputPanel.removeAll();
        inputFields.put(tableName, new ArrayList<>());

        try {
            String query = "SELECT * FROM " + tableName + " WHERE 1=0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                inputPanel.add(new JLabel(metaData.getColumnName(i)));
                JTextField field = new JTextField();
                inputPanel.add(field);
                inputFields.get(tableName).add(field);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private JTable getTableByName(String name) {
        switch (name) {
            case "khachhang": return tableKhachHang;
            case "donhang": return tableDonHang;
            case "sanpham": return tableSanPham;
            case "ChiTietDonHang": return tableChiTietDonHang;
            default: return null;
        }
    }

    private void handleAdd() {
        try {
            ArrayList<JTextField> fields = inputFields.get(currentTable);
            String query = "INSERT INTO " + currentTable + " VALUES (" +
                    "?".repeat(fields.size()).replace("", ", ").substring(2) + ")";
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 0; i < fields.size(); i++) {
                pstmt.setString(i + 1, fields.get(i).getText());
            }
            pstmt.executeUpdate();
            loadTableData(currentTable, getTableByName(currentTable));
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void handleUpdate() {
        try {
            int selectedRow = getTableByName(currentTable).getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Chọn một dòng để sửa.");
                return;
            }

            ArrayList<JTextField> fields = inputFields.get(currentTable);
            DefaultTableModel model = (DefaultTableModel) getTableByName(currentTable).getModel();
            String primaryKeyColumn = model.getColumnName(0);

            String query = "UPDATE " + currentTable + " SET ";
            for (int i = 1; i < fields.size(); i++) {
                query += model.getColumnName(i) + " = ?";
                if (i < fields.size() - 1) query += ", ";
            }
            query += " WHERE " + primaryKeyColumn + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i < fields.size(); i++) {
                pstmt.setString(i, fields.get(i).getText());
            }
            pstmt.setString(fields.size(), fields.get(0).getText());
            pstmt.executeUpdate();

            loadTableData(currentTable, getTableByName(currentTable));
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void handleDelete() {
        try {
            int selectedRow = getTableByName(currentTable).getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Chọn một dòng để xóa.");
                return;
            }

            ArrayList<JTextField> fields = inputFields.get(currentTable);
            DefaultTableModel model = (DefaultTableModel) getTableByName(currentTable).getModel();
            String primaryKeyColumn = model.getColumnName(0);

            String query = "DELETE FROM " + currentTable + " WHERE " + primaryKeyColumn + " = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, fields.get(0).getText());
            pstmt.executeUpdate();

            loadTableData(currentTable, getTableByName(currentTable));
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    private void printOrder() {
        JTable current = getTableByName(currentTable);
        int row = current.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng để in.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) current.getModel();
        StringBuilder sb = new StringBuilder("Thông tin đơn hàng:\n");
        for (int i = 0; i < model.getColumnCount(); i++) {
            sb.append(model.getColumnName(i)).append(": ")
              .append(model.getValueAt(row, i)).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void viewOrderHistory() {
        try {
            String query = "SELECT * FROM donhang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            tableDonHang.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateTotalAmount() {
        try {
            String query = "SELECT SUM(Gia * SoLuong) AS TongTien FROM ChiTietDonHang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Tổng giá trị đơn hàng: " + rs.getDouble("TongTien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderManagement().setVisible(true));
    }
}
