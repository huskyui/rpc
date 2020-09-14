# husky-rpc
居于netty实现的rpc框架，大体上在我没仔细看dubbo源码之前的黑盒写法

### 参考资料
1.https://github.com/pyloque/rpckids
2.https://github.com/crossoverJie/cim
3.https://github.com/wangchenyan/cchat
4.https://cloud.tencent.com/developer/article/1049142 
### 注意
请不要叫我缝合怪
### server
1.读取client端信息
2.根据读取的信息，调用自身的服务
3.将信息返回给client

#### client
请求服务端等待返回信息

#### 服务端大体的实现
但是我不希望是这样实现，这样和spring耦合太高
```java
    public class TestController{
    @RequestMapping(value = "/call",method = RequestMethod.POST)
    public String call(@RequestBody Map<String,String> requestMap) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
        String className = requestMap.get("class");
        String methodName = requestMap.get("method");
        Object[] parameters = new Object[2];
        parameters[0] = "老梁舅红烧狮子头套餐";
        parameters[1] = new Date();
        Class[] parameterClasses = new Class[2];
        parameterClasses[0] = String.class;
        parameterClasses[1] = Date.class;
        Class clazz = Class.forName(className);
        Object object = ApplicationContextHolder.getBean("ChineseCooker",clazz);
        Method method =  clazz.getDeclaredMethod(methodName,parameterClasses);
        method.setAccessible(true);
        Object result = method.invoke(object,parameters);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }
}       


```

### 待解决问题
+ 返回值如何确定，以及如何阻塞响应（使用future?）
+ client请求时，使用java自带的动态代理或者cglib（cglib尝试?），来发送请求
+ 传输使用什么传输协议,json还是google protocol
+ 服务注册发现，使用zookeeper或者使用其他的，阿波罗？
+ 负载均衡，通过读取zookeeper中的server节点，采取负载均衡政策


### 版本一
客户端隐藏实现逻辑，使用动态代理，将请求发送给服务端
### 版本二

实现一个map, key:value => requestId:result
每一个rpc请求的requestId都不一致，
client:在请求服务端时，将requestId带入服务端,并且轮询查看结果集（map）是否有这个requestId
server:服务端完成invoke后，将requestId和结果一并返回
### 版本三
将多个module改成一个module,这里就设计过去思考有点没有注意到，如果我们生成jar时，并不要区分client和server





### quick start

mvn clean install

```xml
            <dependency>
                <groupId>com.husky</groupId>
                <artifactId>hrpc</artifactId>
                <version>1.0</version>
            </dependency>
```

```java

public class Server {
    public static void main(String[] args) {
        MessageHandlerHolder.add(UserService.class,new UserServiceImpl());
        NettyServer nettyServer = new NettyServer(10243);
        nettyServer.start();
    }
}


public class Client {
    public static void main(String[] args) {
        UserService userService = (UserService) DynamicProxy.newProxy(UserService.class);
        for (int i = 0; i < 100; i++) {
            System.out.println(userService.findUserById(i));
        }
    }
}

```





