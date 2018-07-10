```
项目介绍：主要是封装关于对外http请求的，主入口在HttpRestUtil

1、可以根据请求的地址不同在disConfig配置相应内容get获取
2、可以在HttpRestUtilshang上包2层用于请求不同外围系统给出一个统一的方法，策略模式
3、日志可以用aop做，或者在第二点的最外层做
4、用到了springboot的RestTemplateBuilder
可以换成spring的

```