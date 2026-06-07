☕ coffee-data

> 基于 Hadoop 伪分布式环境 + Maven 构建的咖啡销售数据全流程处理与多维度分析项目。包含数据清洗、6 个 MapReduce 统计任务及可视化展示。

## 📌 技术栈
| 类别 | 技术 |
|------|------|
| 大数据框架 | Hadoop（伪分布式）、MapReduce、HDFS |
| 数据迁移 | Sqoop（MySQL ↔ HDFS 双向迁移） |
| 数据库 | MySQL |
| 开发语言 | Java 8+ |
| 构建工具 | Maven 3.x |
| 可视化 |  Matplotlib |

## 📁项目结构

- `pom.xml` — Maven 项目配置文件
- `README.md` — 项目说明文档
- `src/main/java/` — Java 源码根目录
  - `CoffeeCleanBean.java` — 清洗后的数据实体（11个字段），实现 Writable
  - `CoffeeCleanMapper.java` — 数据清洗 Mapper（校验字段、过滤脏数据）
  - `CoffeeCleanDriver.java` — 数据清洗 Driver（Map-only 作业）
  - `CoffeeMapper1.java` — 任务1 Mapper：按月提取销售额和日期
  - `CoffeeReducer1.java` — 任务1 Reducer：计算月总销售额和日均
  - `CoffeeDriver1.java` — 任务1 Driver
  - `CoffeeBean2.java` — 任务2 自定义 WritableComparable，用于排序
  - `CoffeeMapper2.java` — 任务2 Mapper：提取咖啡名和金额
  - `CoffeeReducer2.java` — 任务2 Reducer：累加销售额并输出前5名
  - `CoffeeDriver2.java` — 任务2 Driver
  - `CoffeeBean3.java` — 任务3 Bean：时段计数（Writable）
  - `CoffeeMapper3.java` — 任务3 Mapper：提取时段
  - `CoffeeReducer3.java` — 任务3 Reducer：累加时段出现次数
  - `CoffeeDriver3.java` — 任务3 Driver
  - `CoffeeBean4.java` — 任务4 Bean：价格区间计数
  - `CoffeeMapper4.java` — 任务4 Mapper：金额映射到价格区间
  - `CoffeeReducer4.java` — 任务4 Reducer：累加区间订单数
  - `CoffeeDriver4.java` — 任务4 Driver
  - `CoffeeBean5.java` — 任务5 Bean：工作日/周末销量计数
  - `CoffeeMapper5.java` — 任务5 Mapper：提取星期序号，分类工作日/周末
  - `CoffeeReducer5.java` — 任务5 Reducer：累加两类销量
  - `CoffeeDriver5.java` — 任务5 Driver
  - `CoffeeBean6.java` — 任务6 Bean：咖啡销售次数
  - `CoffeeMapper6.java` — 任务6 Mapper：提取咖啡名
  - `CoffeeReducer6.java` — 任务6 Reducer：累加咖啡销售次数
  - `CoffeeDriver6.java` — 任务6 Driver
- `src/main/resources/` — 资源目录
  - `Coffe_sales_no_header.csv` — 原始数据（3547条，无表头，11个字段）
- `target/` — Maven 编译输出目录（自动生成）
  - `classes/` — 编译后的 .class 文件

text

### 目录与文件说明

| 路径 | 说明 |
|------|------|
| `pom.xml` | Maven 配置文件，声明 Hadoop、Sqoop 等依赖，并指定 Java 版本和打包插件。 |
| `src/main/java/` | 所有 Java 源文件（无子包，平铺放置）。每个 MapReduce 任务有独立的 Mapper、Reducer、Driver，任务2~6 还有对应的自定义 Bean。 |
| `src/main/resources/` | 存放原始数据 CSV 文件，运行时可通过 `hadoop jar` 传入路径，也可先上传至 HDFS。 |
| `target/` | Maven 编译后生成的目录，包含 `.class` 文件和打包的 JAR。 |
| `visualization/` |（建议添加）存放 Python 或 ECharts 生成的可视化图表图片。 |
| `report/` |（建议添加）存放课程实验报告 PDF。 |

## 🧹数据清洗（Map-only 作业）

**输入**：`src/main/resources/Coffe_sales_no_header.csv`（11 个字段，无表头）  
**输出**：HDFS `/root/mapreduce/output/part-r-00000`

| 组件 | 说明 |
|------|------|
| `CoffeeCleanBean` | 实现 `Writable`，包含 11 个字段（小时、支付方式、金额、咖啡名、时段、星期、月份名、星期序号、月份序号、日期、时间）。提供序列化/反序列化方法。 |
| `CoffeeCleanMapper` | 读取一行，按逗号分割。校验：字段数=11，小时∈[0,24]，金额>0，星期序号∈[1,7]，月份序号∈[1,12]。合法数据封装为 Bean，输出 `<NullWritable, Text>`（逗号分隔字符串）。异常数据直接跳过。 |
| `CoffeeCleanDriver` | 配置 Job（只有 Mapper），设置输入路径（原始数据）和输出路径（清洗后结果）。输出目录若存在则删除。 |

## 📊分析任务 1～6

所有任务**均以清洗后的数据**（`/root/mapreduce/output/part-r-00000`）作为输入。

---

### 任务1：按月统计销售总额与日均销售额

- **Mapper1**：从清洗后数据的第9个字段（日期 `yyyy-MM-dd`）提取月份（`yyyy-MM`）作为 key；从第2个字段（金额）提取数值。输出两条记录：`<月份, "M|金额">` 和 `<月份, "D|日期">`。
- **Reducer1**：对于同一月份，累加 `M|金额` 得到总销售额；用 `HashSet` 收集 `D|日期` 得到当月天数；计算日均销售额 = 总销售额 / 天数。输出 `<月份, 总销售额\t天数\t日均销售额>`。
- **Driver1**：设置 Mapper 和 Reducer，输出键值均为 `Text`。输入清洗后数据，输出到 `/root/mapreduce/output1`。

---

### 任务2：按咖啡品类统计总销售额及排名（输出前5名）

- **Bean2**：实现 `WritableComparable`，按 `totalSales` 降序排序（`compareTo` 方法）。
- **Mapper2**：提取咖啡名称（字段3）作为 key，金额（字段2）作为 `DoubleWritable` value。输出 `<咖啡名, 金额>`。
- **Reducer2**：累加同一咖啡名的金额得到总销售额；使用 `TreeMap<Double, String>` 按销售额降序存储（只保留前5名）。`cleanup` 方法输出排名：“第N名，总销售额：xx元”。
- **Driver2**：设置 Map 输出 `<Text, DoubleWritable>`，最终输出 `<Text, Text>`。输出到 `/root/mapreduce/output2`。

---

### 任务3：识别高峰时段（Morning/Afternoon/Night）

- **Bean3**：实现 `Writable`，包含 `count` 字段，提供 `add()` 累加方法。
- **Mapper3**：提取时段字段（字段4）作为 key，输出 `<时段, Bean3(计数=1)>`。
- **Reducer3**：累加同一时段的计数，输出 `<时段, Bean3(总计数)>`。
- **Driver3**：Map 输出 `<Text, CoffeeBean3>`，最终输出相同类型。输出到 `/root/mapreduce/output3`。

---

### 任务4：划分价格区间，定位最受欢迎价格带

- **Bean4**：实现 `Writable`，包含 `count` 字段（订单数），用于累加。
- **Mapper4**：读取金额（字段2），映射到价格区间：0-5,5-10,10-15,…,35-40,40+。输出 `<价格区间, Bean4(计数=1)>`。
- **Reducer4**：累加同一价格区间的订单数，输出 `<价格区间, Bean4(总计数)>`。
- **Driver4**：输出到 `/root/mapreduce/output4`。

---

### 任务5：工作日 vs 周末销量对比

- **Bean5**：实现 `Writable`，包含 `count` 字段（销量/订单数）。
- **Mapper5**：读取星期序号（字段8，1-7）。1-5 为“工作日”，6-7 为“周末”。输出 `<类型, Bean5(计数=1)>`。
- **Reducer5**：累加工作日的订单数和周末的订单数，输出 `<类型, Bean5(总计数)>`。
- **Driver5**：输出到 `/root/mapreduce/output5`。

---

### 任务6：统计销售最好的咖啡（按销售次数）

- **Bean6**：实现 `Writable`，包含 `saleCount` 字段（销售次数）。
- **Mapper6**：提取咖啡名称（字段3）作为 key，输出 `<咖啡名, Bean6(计数=1)>`。
- **Reducer6**：累加同一咖啡名的销售次数，输出 `<咖啡名, Bean6(总次数)>`。
- **Driver6**：输出到 `/root/mapreduce/output6`。
