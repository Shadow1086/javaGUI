## 学生信息管理系统（CSV 方案）实施计划

### 范围与目标
- 在 `Homework/StudentManage` 下实现基于 Swing 的学生信息管理系统
- 三区布局：顶部按钮区、中间表格区、底部表单区
- 功能按钮：新增、删除、修改、清空、保存（CSV 导出）、导入（CSV 读取）
- 数据持久化：CSV（UTF-8，无 BOM），默认路径：项目根目录 `data/students.csv`

### 新增/修改的核心文件
- 新增：`src/main/java/Homework/StudentManage/ui/StudentManagerApp.java`（启动类，含 `main`）
- 新增：`src/main/java/Homework/StudentManage/ui/StudentManagerFrame.java`（主窗口：三段式布局与事件）
- 新增：`src/main/java/Homework/StudentManage/StudentTableModel.java`（继承 `AbstractTableModel`，管理表格数据）
- 新增：`src/main/java/Homework/StudentManage/StudentCsvRepository.java`（CSV 读写：load/save）
- 复用：`src/main/java/Homework/StudentManage/Student.java`（学生实体，必要时补充字段/方法）
- 新增：`data/students.csv`（首次运行自动创建，含表头）

### 关键设计要点
- 表头字段顺序：`name,studentId,gender,age`
- 表单校验：
  - 姓名：非空
  - 学号：非空且唯一
  - 性别：限定“男/女”
  - 年龄：1-150 的整数
- 表格选中行回填：选择表格行时，将数据回填到底部输入区
- 修改流程：表单修改后点击“修改”→ 写回模型 → 刷新表格
- 按钮状态：未选中行禁用“删除/修改”
- 保存/导入：
  - 保存：将 `StudentTableModel` 的数据导出到 `data/students.csv`（写入表头）
  - 导入：从 `data/students.csv` 读取并替换当前表格数据；覆盖前二次确认

### 类职责与方法骨架
- `StudentTableModel`（列：姓名、学号、性别、年龄）
  - `addStudent(Student s)`
  - `removeAt(int rowIndex)`
  - `updateAt(int rowIndex, Student s)`
  - `getStudentAt(int rowIndex)`
  - `setStudents(List<Student> list)`
  - `getStudents() : List<Student>`
- `StudentCsvRepository`
  - `List<Student> load(Path csvPath)`：UTF-8，无 BOM；跳过首行表头
  - `void save(Path csvPath, List<Student> students)`：若文件不存在则创建并写表头
- `StudentManagerFrame`
  - 顶部：新增/删除/修改/清空/保存/导入 按钮与监听
  - 中部：`JTable` + `StudentTableModel` + `ListSelectionListener`
  - 底部：姓名（`JTextField`）、学号（`JTextField`）、性别（`JComboBox<String>` 男/女）、年龄（`JTextField`）
  - 对话框：`JOptionPane` 用于校验提示与覆盖确认

### 文件与路径约定
- CSV 路径：项目根目录 `data/students.csv`
- 编码：UTF-8（无 BOM）
- 分隔符：逗号（字段不包含逗号；如需转义可后续升级为 RFC4180）

### 交互与线程
- 所有 UI 更新在 EDT：`SwingUtilities.invokeLater`
- 大文件导入/保存可选用 `SwingWorker` 防止界面卡顿（后续优化）

### 验收清单
- 可新增学生，行内数据显示正确
- 选中行可删除或修改，修改后表格刷新
- “清空”仅清除输入区，不影响表格数据
- “保存”生成/更新 `data/students.csv`，首行包含表头
- “导入”可正确读取 CSV 并覆盖当前表格数据（覆盖前有确认）

### 后续可选增强
- 使用 Apache Commons CSV 或 OpenCSV 做稳健 CSV 解析
- 使用 `JFileChooser` 支持任意路径导入/导出
- 增加日志（SLF4J + Logback）与少量单测（JUnit5）

### 若改用数据库（学习路线提示）
- 选型：SQLite（首选）或 H2（Java 原生）
- JDBC：`Connection / PreparedStatement / ResultSet` 与 try-with-resources
- 表结构：`students(id PK, name, student_id UNIQUE, gender, age)`
- 事务：批量导入/批量修改使用事务提交
- 分层：DAO（`StudentDao`）+（可选）Service；UI 调用 DAO/Service
- Swing 集成：耗时 DB 操作用 `SwingWorker`，结果映射到 `StudentTableModel`

### 学习知识清单（建议掌握）
1. Swing 基础组件与布局
- 事件分发线程（EDT）与 `SwingUtilities.invokeLater`
- 布局管理：`BorderLayout`、`FlowLayout`、`GridBagLayout`（按需）
- 常用组件：`JPanel`、`JLabel`、`JTextField`、`JComboBox`、`JButton`、`JScrollPane`
- 表格：`JTable`、`AbstractTableModel`、`ListSelectionModel`、列渲染与编辑器基础
- 对话框与文件选择：`JOptionPane`、`JFileChooser`

2. 表单校验与交互逻辑
- 必填、唯一性、范围校验与统一提示
- 按钮状态管理：选中行时启用“删除/修改”等
- 模型与视图解耦（MVC 思想）：UI / TableModel / Repository

3. 文件与 CSV 读写
- 字符集与编码：UTF-8（无 BOM）
- Java I/O 与 NIO：`Path`、`Files`、`BufferedReader/Writer`
- CSV 基础与库选型：Apache Commons CSV 或 OpenCSV 的基本用法
- 异常与资源管理：try-with-resources、错误提示与兜底策略

4. 并发与性能（面向桌面应用）
- `SwingWorker` 处理耗时任务（导入/保存）避免界面卡顿
- 进度反馈与取消（可选）

5. 工程与质量保障
- Maven 依赖管理与项目结构（`src/main/java`、`resources`）
- 日志：SLF4J API + Logback 实现（基础配置）
- 简单单元测试：JUnit5（CSV 读写与校验逻辑）
- 基本代码风格与注释规范

6. 若采用数据库（可选进阶）
- JDBC API：`DriverManager`、`Connection`、`PreparedStatement`、`ResultSet`
- 事务与批处理、参数化查询（防 SQL 注入）
- 数据库选型与驱动：SQLite 或 H2 的引入与连接 URL
- 数据建模：表结构、主键/唯一约束、索引、基础 SQL（CRUD）
- DAO 模式与分层设计；与 Swing 集成（`SwingWorker`）

7. UI 体验与进阶特性（可选）
- 表格排序与过滤：`RowSorter` / `TableRowSorter`
- 国际化：`ResourceBundle`（可选）
- 主题与外观：`LookAndFeel`（可选）
- 导入导出容错与边界处理（空值、非法行、重复学号策略）

