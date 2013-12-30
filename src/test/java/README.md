测试时需要将org.antstudio.rbac.domain.init.Initializer 注册取消
或者等待event处理结果，如果使用异步事件模型，逻辑会马上执行完毕并且关闭容器，从而事件无法执行.

并且需要修改源码编译包含的文件，否则eclipse只会编译java源文件而不会将xml也发布。具体设置步骤如下：
项目-->右键-->properties-->build path-->source-->moon/src/main/java-->included
-->edit-->在弹出框中选择add-->输入 **/*.xml保存即可