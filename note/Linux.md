```shell
sudo systemctl status // Linux查看全部运行程序
```

# docker

```shell
docker ps --no-trunc //不省略

docker build -t 名 . // Dockerfile文件创建镜像

docker exec -it id /bin/bash  // 进入容器
```

[Docker — 从入门到实践 | Docker 从入门到实践 (docker-practice.com)](https://vuepress.mirror.docker-practice.com/)

# Git

## 分支操作 branch

```shell
git branch -v // 查看分支列表

git branch -m main // 将当前分支命名main，已有名将创建新分支

git branch -d master // 删除分支

git checkout main // 切换分支

git merge master // 合并master分支到当前分支
```

