# 基础镜像，openjkd使用8版本
FROM hub.c.163.com/library/java:8
# 镜像名
LABEL name="Robot"
# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /Robot/
# 地址|上传名字
ADD target/miraiBot-1.0-SNAPSHOT.jar Robot.jar
ADD device.json device.json
# 同步服务器时间
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 启动容器时的进程命令
ENTRYPOINT ["java","-jar","/Robot.jar"]