### generate protoc java 
首先需要下载proto压缩包，从github的release中下载
将proto的bin目录放入到环境变量中
```shell
protoc --java_out=. .\Message.proto
```
