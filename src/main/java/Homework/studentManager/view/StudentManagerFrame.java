package homework.studentManager.view;

import homework.studentManager.factory.AppFactory;
import homework.studentManager.fileService.StudentFileService;
import homework.studentManager.model.Student;
import homework.studentManager.studentService.StudentService;
import homework.studentManager.studentUtil.ValidationUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ClassName: StudentManagerFrame
 * Description:
 * 
 * 主窗口 - 学生信息管理系统
 * 
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-11-11 21:03:01
 */
public class StudentManagerFrame {
    // 主窗口
    private JFrame frame = new JFrame("学生信息管理系统");

    private final StudentService studentService = AppFactory.createStudetnService();
    private final StudentFileService fileService = new StudentFileService(AppFactory.createStudentDao());

    // 表头（列名）：姓名、学号、性别、年龄、创建时间、修改时间
    private String[] columnNames = { "姓名", "学号", "性别", "年龄", "创建时间", "修改时间", "ID" };

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 创建表格模型
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // 创建JTable表格组件
    private JTable table = new JTable(tableModel);

    // 顶部按钮
    private JButton btnAdd = new JButton("新增");
    private JButton btnDelete = new JButton("删除");
    private JButton btnModify = new JButton("修改");
    private JButton btnClear = new JButton("清空");
    private JButton btnSave = new JButton("保存");
    private JButton btnImport = new JButton("导入");

    // 搜索区
    private JTextField tfSearch = new JTextField(16);
    private JRadioButton rbSearchById = new JRadioButton("按学号");
    private JRadioButton rbSearchByName = new JRadioButton("按姓名");
    private JButton btnSearch = new JButton("搜索");
    private JButton btnSearchClear = new JButton("清除");

    // 底部输入框
    private JTextField tfName = new JTextField(15);
    private JTextField tfStuId = new JTextField(15);
    private JComboBox<String> cbSex = new JComboBox<>(new String[] { "男", "女" });
    private JTextField tfAge = new JTextField(15);

    // 当前选中的行索引
    private int selectedRow = -1;
    private JDialog loadingDialog;

    /**
     * 初始化界面
     */
    public void init() {
        frame.setLayout(new BorderLayout(10, 10));

        // 1. 创建顶部按钮区
        JPanel topPanel = createTopPanel();
        frame.add(topPanel, BorderLayout.NORTH);

        // 2. 创建中间表格区
        JPanel centerPanel = createCenterPanel();
        frame.add(centerPanel, BorderLayout.CENTER);

        // 3. 创建底部输入区
        JPanel bottomPanel = createBottomPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 5. 初始化按钮状态
        updateButtonState();

        // 6. 添加表格选中监听器
        addTableSelectionListener();

        // 7. 添加按钮事件监听器
        addButtonListeners();

        // 窗口设置
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // 4. 加载数据（异步，避免阻塞EDT）
        loadTableDataAsync();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            tableModel.addRow(new Object[] {
                    student.getName(),
                    student.getStuId(),
                    student.getSex(),
                    String.valueOf(student.getAge()),
                    formatDateTime(student.getCreateTime()),
                    formatDateTime(student.getUpdateTime()),
                    student.getId()
            });
        }
    }

    private void loadTableDataAsync() {
        setControlsEnabled(false);
        showLoadingDialog();
        new SwingWorker<List<Student>, Void>() {
            @Override
            protected List<Student> doInBackground() {
                return studentService.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<Student> students = get();
                    tableModel.setRowCount(0);
                    for (Student student : students) {
                        tableModel.addRow(new Object[] {
                                student.getName(),
                                student.getStuId(),
                                student.getSex(),
                                String.valueOf(student.getAge()),
                                formatDateTime(student.getCreateTime()),
                                formatDateTime(student.getUpdateTime()),
                                student.getId()
                        });
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            "加载数据失败，请检查数据库连接",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    setControlsEnabled(true);
                    hideLoadingDialog();
                    updateButtonState();
                }
            }
        }.execute();
    }

    /**
     * 创建顶部按钮区
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnImport);

        JPanel searchPanel = createSearchPanel();

        panel.add(buttonPanel);
        panel.add(searchPanel);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));

        ButtonGroup group = new ButtonGroup();
        group.add(rbSearchById);
        group.add(rbSearchByName);
        rbSearchById.setSelected(true);

        panel.add(new JLabel("关键词:"));
        panel.add(tfSearch);
        panel.add(rbSearchById);
        panel.add(rbSearchByName);
        panel.add(btnSearch);
        panel.add(btnSearchClear);
        return panel;
    }

    /**
     * 创建中间表格区
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 设置表格属性
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        TableColumn sexColumn = table.getColumnModel().getColumn(2);
        sexColumn.setPreferredWidth(60);
        sexColumn.setMinWidth(60);
        sexColumn.setMaxWidth(60);

        TableColumn ageColumn = table.getColumnModel().getColumn(3);
        ageColumn.setPreferredWidth(60);
        ageColumn.setMinWidth(60);
        ageColumn.setMaxWidth(60);

        TableColumn idColumn = table.getColumnModel().getColumn(6);
        table.removeColumn(idColumn);

        // 添加滚动条
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建底部输入区
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 第一行：姓名
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("姓名:"), gbc);

        gbc.gridx = 1;
        panel.add(tfName, gbc);

        // 第一行：学号
        gbc.gridx = 2;
        panel.add(new JLabel("学号:"), gbc);

        gbc.gridx = 3;
        panel.add(tfStuId, gbc);

        // 第二行：性别
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("性别:"), gbc);

        gbc.gridx = 1;
        panel.add(cbSex, gbc);

        // 第二行：年龄
        gbc.gridx = 2;
        panel.add(new JLabel("年龄:"), gbc);

        gbc.gridx = 3;
        panel.add(tfAge, gbc);

        return panel;
    }

    /**
     * 添加表格选中监听器 - 实现选中行自动回填
     */
    private void addTableSelectionListener() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int viewRow = table.getSelectedRow();
                    selectedRow = (viewRow == -1) ? -1 : table.convertRowIndexToModel(viewRow);
                    if (selectedRow != -1) {
                        // 回填数据到输入框
                        tfName.setText(tableModel.getValueAt(selectedRow, 0).toString());
                        tfStuId.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        cbSex.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                        tfAge.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    }
                    // 更新按钮状态
                    updateButtonState();
                }
            }
        });
    }

    /**
     * 添加按钮事件监听器
     */
    private void addButtonListeners() {
        // 新增按钮
        btnAdd.addActionListener(e -> handleAdd());

        // 删除按钮
        btnDelete.addActionListener(e -> handleDelete());

        // 修改按钮
        btnModify.addActionListener(e -> handleModify());

        // 清空按钮
        btnClear.addActionListener(e -> {
            clearInputFields();
            table.clearSelection();
            selectedRow = -1;
            updateButtonState();
        });

        // 保存按钮
        btnSave.addActionListener(e -> handleSave());

        // 导入按钮
        btnImport.addActionListener(e -> handleImport());

        // 搜索按钮
        btnSearch.addActionListener(e -> handleSearch());

        // 清除搜索
        btnSearchClear.addActionListener(e -> clearSearch());
    }

    // ==================== 以下是需要你实现的功能函数 ====================

    /**
     * 处理新增学生功能
     * 
     * 需要实现的逻辑：
     * 1. 从输入框获取数据：
     * - 姓名：tfName.getText()
     * - 学号：tfStuId.getText()
     * - 性别：cbSex.getSelectedItem().toString()
     * - 年龄：tfAge.getText()
     * 
     * 2. 数据校验：
     * - 姓名不能为空
     * - 学号不能为空，且不能与表格中已有的学号重复
     * - 年龄必须是1-150之间的整数
     * - 如果校验失败，使用 JOptionPane.showMessageDialog() 提示用户
     * 
     * 3. 添加到表格：
     * - 使用 tableModel.addRow(new Object[]{姓名, 学号, 性别, 年龄})
     * 
     * 4. 清空输入框：
     * - 调用 clearInputFields() 方法
     * 
     * 提示：可以创建 Student 对象来管理学生数据
     */
    private void handleAdd() {
        String name = tfName.getText().trim();
        String stuId = tfStuId.getText().trim();
        String sex = cbSex.getSelectedItem().toString();
        String ageStr = tfAge.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "姓名不能为空");
            return;
        }
        if (stuId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "学号不能为空");
            return;
        }
        if (isDuplicateStudentId(stuId, -1)) {
            JOptionPane.showMessageDialog(frame, "学号已存在，请更换学号");
            return;
        }
        if (!isValidAge(ageStr)) {
            JOptionPane.showMessageDialog(frame, "年龄必须是1-120之间的整数");
            return;
        }

        int age = Integer.parseInt(ageStr);
        Student student = new Student(stuId, name, sex, age);
        ValidationUtil.ValidationResult validation = ValidationUtil.stuInfoIsValidate(student);
        if (!validation.isValid()) {
            JOptionPane.showMessageDialog(frame, validation.getMessage());
            return;
        }

        boolean success = studentService.addStudent(student);
        if (!success) {
            JOptionPane.showMessageDialog(frame, "新增失败，请检查输入或数据库连接");
            return;
        }

        loadTableData();
        clearInputFields();
        table.clearSelection();
        selectedRow = -1;
        updateButtonState();
    }

    /**
     * 处理删除学生功能
     * 
     * 需要实现的逻辑：
     * 1. 检查是否选中了行（selectedRow != -1）
     * 
     * 2. 弹出确认对话框：
     * - 使用 JOptionPane.showConfirmDialog() 询问用户是否确认删除
     * 
     * 3. 如果用户确认删除：
     * - 从表格中删除该行：tableModel.removeRow(selectedRow)
     * - 清空输入框：clearInputFields()
     * - 重置选中行：selectedRow = -1
     * - 更新按钮状态：updateButtonState()
     * 
     * 提示：删除操作可能需要同步到数据源（如果有的话）
     */
    private void handleDelete() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "请先选择需要删除的学生");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame,
                "确定要删除选中的学生信息吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Student student = buildStudentFromRow(selectedRow);
            boolean success = studentService.deleteStudent(student);
            if (!success) {
                JOptionPane.showMessageDialog(frame, "删除失败，请检查数据库连接");
                return;
            }
            loadTableData();
            clearInputFields();
            selectedRow = -1;
            updateButtonState();
        }
    }

    /**
     * 处理修改学生功能
     * 
     * 需要实现的逻辑：
     * 1. 检查是否选中了行（selectedRow != -1）
     * 
     * 2. 从输入框获取修改后的数据：
     * - 姓名：tfName.getText()
     * - 学号：tfStuId.getText()
     * - 性别：cbSex.getSelectedItem().toString()
     * - 年龄：tfAge.getText()
     * 
     * 3. 数据校验（同新增功能）：
     * - 姓名不能为空
     * - 学号不能为空，且不能与其他行的学号重复（排除当前行）
     * - 年龄必须是1-150之间的整数
     * 
     * 4. 更新表格数据：
     * - tableModel.setValueAt(姓名, selectedRow, 0)
     * - tableModel.setValueAt(学号, selectedRow, 1)
     * - tableModel.setValueAt(性别, selectedRow, 2)
     * - tableModel.setValueAt(年龄, selectedRow, 3)
     * 
     * 5. 清空输入框并取消选中：
     * - clearInputFields()
     * - table.clearSelection()
     * - selectedRow = -1
     * - updateButtonState()
     * 
     * 提示：修改操作可能需要同步到数据源（如果有的话）
     */
    private void handleModify() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "请先选择需要修改的学生");
            return;
        }

        String name = tfName.getText().trim();
        String stuId = tfStuId.getText().trim();
        String sex = cbSex.getSelectedItem().toString();
        String ageStr = tfAge.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "姓名不能为空");
            return;
        }
        if (stuId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "学号不能为空");
            return;
        }
        if (isDuplicateStudentId(stuId, selectedRow)) {
            JOptionPane.showMessageDialog(frame, "学号已存在，请更换学号");
            return;
        }
        if (!isValidAge(ageStr)) {
            JOptionPane.showMessageDialog(frame, "年龄必须是1-120之间的整数");
            return;
        }

        int age = Integer.parseInt(ageStr);
        Student student = new Student(stuId, name, sex, age);
        student.setId(parseStudentId(selectedRow));
        ValidationUtil.ValidationResult validation = ValidationUtil.stuInfoIsValidate(student);
        if (!validation.isValid()) {
            JOptionPane.showMessageDialog(frame, validation.getMessage());
            return;
        }

        boolean success = studentService.updateStuInfo(student);
        if (!success) {
            JOptionPane.showMessageDialog(frame, "修改失败，请检查输入或数据库连接");
            return;
        }

        loadTableData();
        clearInputFields();
        table.clearSelection();
        selectedRow = -1;
        updateButtonState();
    }

    /**
     * 处理保存功能（导出到CSV文件）
     */
    private void handleSave() {
        File file = null;
        try {
            // 显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("students.csv"));
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                }

                @Override
                public String getDescription() {
                    return "CSV文件(*.csv)";
                }
            });
            int result = fileChooser.showSaveDialog(frame);
            // 5. 如果用户点击了"保存"按钮
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            } else {
                return;
            }
            // 6. 确保文件扩展名是 .csv
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            boolean success = fileService.exportFile(file);
            if (!success) {
                JOptionPane.showMessageDialog(frame,
                        "保存失败，请检查文件路径或数据源",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 使用 HTML 格式化路径显示，避免乱码
            String message = "<html><body style='width: 400px'>" +
                    "<p>保存成功！</p>" +
                    "<p>文件路径：<br>" + file.getAbsolutePath() + "</p>" +
                    "</body></html>";
            JOptionPane.showMessageDialog(frame, message, "保存成功",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "保存失败：" + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 处理导入功能（从CSV文件读取）
     * 
     * 需要实现的逻辑：
     * 1. 选择要导入的CSV文件：
     * - 使用 JFileChooser 让用户选择文件
     * - 或者使用默认路径：data/students.csv
     * 
     * 2. 读取CSV文件：
     * - 文件格式：UTF-8编码
     * - 跳过第一行表头
     * - 解析每一行数据，按逗号分隔
     * - 示例行：张三,2021001,男,20
     * 
     * 3. 导入前确认：
     * - 使用 JOptionPane.showConfirmDialog() 询问用户
     * - 提示：导入会覆盖当前表格中的所有数据
     * 
     * 4. 清空当前表格并加载新数据：
     * - tableModel.setRowCount(0) 清空表格
     * - 遍历读取的数据，使用 tableModel.addRow() 添加到表格
     * 
     * 5. 异常处理：
     * - 捕获 IOException、FileNotFoundException 等异常
     * - 数据格式错误时给出友好提示
     * - 使用 JOptionPane 提示导入成功或失败
     * 
     * 6. 导入完成后：
     * - 清空输入框：clearInputFields()
     * - 取消选中：table.clearSelection()
     * 
     * 提示：可以使用 BufferedReader 或 FileReader 读取文件
     */
    private void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV文件(*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        int confirm = JOptionPane.showConfirmDialog(frame,
                "导入会覆盖当前表格中的所有数据，是否继续？",
                "确认导入",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        deleteAllStudents();
        int count = fileService.importFile(file);
        if (count < 0) {
            JOptionPane.showMessageDialog(frame,
                    "导入失败，请检查文件内容或格式",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadTableData();
        clearInputFields();
        table.clearSelection();
        selectedRow = -1;
        updateButtonState();
        JOptionPane.showMessageDialog(frame, "导入成功，共导入 " + count + " 条记录");
    }

    private void handleSearch() {
        String keyword = tfSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "请输入搜索关键词");
            return;
        }

        List<Student> results;
        if (rbSearchById.isSelected()) {
            results = studentService.findById(keyword);
        } else {
            results = studentService.findByName(keyword);
        }

        if (results == null || results.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "未找到匹配的学生信息");
            return;
        }

        tableModel.setRowCount(0);
        for (Student student : results) {
            tableModel.addRow(new Object[] {
                    student.getName(),
                    student.getStuId(),
                    student.getSex(),
                    String.valueOf(student.getAge()),
                    formatDateTime(student.getCreateTime()),
                    formatDateTime(student.getUpdateTime()),
                    student.getId()
            });
        }

        table.clearSelection();
        selectedRow = -1;
        updateButtonState();
    }

    private void clearSearch() {
        tfSearch.setText("");
        loadTableData();
        table.clearSelection();
        selectedRow = -1;
        updateButtonState();
    }

    // ==================== 辅助方法（可选使用） ====================

    /**
     * 校验学号是否重复
     * 
     * @param stuId      要检查的学号
     * @param excludeRow 要排除的行索引（修改时使用，新增时传-1）
     * @return true表示重复，false表示不重复
     */
    private boolean isDuplicateStudentId(String stuId, int excludeRow) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i == excludeRow) {
                continue; // 跳过当前编辑的行
            }
            String existingId = tableModel.getValueAt(i, 1).toString();
            if (existingId.equals(stuId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验年龄是否合法
     * 
     * @param ageStr 年龄字符串
     * @return true表示合法，false表示不合法
     */
    private boolean isValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age >= 1 && age <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Student buildStudentFromRow(int row) {
        String name = tableModel.getValueAt(row, 0).toString();
        String stuId = tableModel.getValueAt(row, 1).toString();
        String sex = tableModel.getValueAt(row, 2).toString();
        int age = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
        Student student = new Student(stuId, name, sex, age);
        student.setId(parseStudentId(row));
        return student;
    }

    private int parseStudentId(int row) {
        Object value = tableModel.getValueAt(row, 6);
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void deleteAllStudents() {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            studentService.deleteStudent(student);
        }
    }

    /**
     * 更新按钮状态 - 根据是否选中行来启用/禁用按钮
     */
    private void updateButtonState() {
        boolean hasSelection = (selectedRow != -1);
        btnDelete.setEnabled(hasSelection);
        btnModify.setEnabled(hasSelection);
    }

    private void setControlsEnabled(boolean enabled) {
        btnAdd.setEnabled(enabled);
        btnClear.setEnabled(enabled);
        btnSave.setEnabled(enabled);
        btnImport.setEnabled(enabled);
        btnDelete.setEnabled(enabled && selectedRow != -1);
        btnModify.setEnabled(enabled && selectedRow != -1);
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog();
        }
        if (!loadingDialog.isVisible()) {
            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
        }
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(false));
        }
    }

    private JDialog createLoadingDialog() {
        JDialog dialog = new JDialog(frame, "提示", false);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JLabel label = new JLabel("正在加载数据...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));

        JButton btnClose = new JButton("关闭");
        btnClose.addActionListener(e -> dialog.setVisible(false));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        content.add(label, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnClose);
        content.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setSize(260, 140);
        dialog.setLocationRelativeTo(frame);
        return dialog;
    }

    /**
     * 清空输入框
     */
    private void clearInputFields() {
        tfName.setText("");
        tfStuId.setText("");
        cbSex.setSelectedIndex(0);
        tfAge.setText("");
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

}
