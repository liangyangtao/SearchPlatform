此说明意在统一代码规范和格式以及所使用的技术

使用技术说明：
	1、连接Mysql数据库使用mybatis，相关代码以及Mapper文件使用mybatis-generator自动生成
	2、连接其他Http类型接口使用HTTPClient，版本HTTPClient4.3
	3、连接ES使用ES的java客户端
	4、对外的restful接口使用Jersey，版本Jersey1.8
	5、DI工具使用Spring，版本3.0.5
	6、日志记录工具使用Log4J
	7、单元测试工具使用Junit
	
包说明：
	1、com.unbank.db:关系型数据库（Mysql）的操作类和相关Mapper文件
	2、com.unbank.entity:对象实体
	3、com.unbank.es:ES操作相关
	4、com.unbank.net:HTTP网络操作
	5、com.unbank.rest:对外restful接口
	6、com.unbank.values:系统常量
	7、其他包可以自行添加。
	
资源文件以及配置文件等：
	1、资源文件和配置文件等统一放置与src/main/resources目录下
	
Junit测试类：
	1、测试类统一放置于src/test/java下
	2、测试类应该包含联通ES、联通Mysql、联通图数据借口等基本功能的验证
	
日志记录：
	1、所有用户操作都需要进行日志记录。
	2、所有try/catch的catch中都需要对异常进行日志记录。

项目管理：
	1、本项目使用maven进行管理项目所需Jar包以及打包等过程。
	

