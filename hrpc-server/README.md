# husky-rpc
居于netty实现的rpc框架，大体上在我没仔细看dubbo源码之前的黑盒写法

### server
1.读取client端信息
2.根据读取的信息，调用自身的服务
3.将信息返回给client



#### client
请求服务端等待返回信息

#### 服务端大体的实现
但是我不希望是这样实现，这样和spring耦合太高
```java
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


```

