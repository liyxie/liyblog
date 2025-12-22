<template>
  <div class="article-container container" v-sy-loading="fullscreenLoading">
    <!-- 左侧点赞和打赏 -->
    <div class="left-sidbarnav" :style="{ left: left }">
      <!-- <el-tooltip class="item" effect="dark" content="点赞" placement="left">
        <div class="left-item hand-style" title="点赞" @click="doLike(article.id)">
          <el-badge :value="article.likeCount == null ? 0 : article.likeCount" class="item">
            <span>
              <i v-if="article.isLike" class="iconfont icon-dianzan4"></i>
              <i v-else class="iconfont icon-dianzan1"></i>
            </span>
          </el-badge>
        </div>
      </el-tooltip>

      <el-tooltip class="item" effect="dark" content="收藏" placement="left">
        <div class="left-item hand-style" title="收藏" @click="doCollect">
          <el-badge :value="article.collectCount" class="item">
            <span style="font-size: 20px">
              <el-icon v-if="article.isCollect">
                <Star />
              </el-icon>
              <el-icon v-else>
                <StarFilled />
              </el-icon>
            </span>
          </el-badge>
        </div>
      </el-tooltip>

      <el-tooltip class="item" effect="dark" content="评论" placement="left" v-if="siteStore.getWebInfo.openComment">
        <div class="left-item hand-style" title="评论" @click="handleNodeClick('comment')">
          <el-badge :value="article.commentCount" class="item">
            <span>
              <el-icon>
                <ChatLineRound />
              </el-icon>
            </span>
          </el-badge>
        </div>
      </el-tooltip> -->

      <!-- <el-tooltip class="item" effect="dark" content="开启沉浸式阅读" placement="left">
        <div class="left-item hand-style" title="开启沉浸式阅读" @click="handleImmerse">
          <span>
            <i class="iconfont icon-full-screen"></i>
          </span>
        </div>
      </el-tooltip> -->

      <el-tooltip class="item" effect="dark" content="打赏" placement="left" v-if="siteStore.getWebInfo.openAdmiration">
        <div class="left-item rewardMain hand-style" title="打赏">
          <span class="reward-btn">
            <i class="iconfont icon-dashang1"></i>
          </span>
          <!-- 二维码 -->
          <div class="rewardItem">
            <span>
              <img class="reward-img" :src="siteStore.getWebInfo.aliPay" />
            </span>
            <span>
              <img class="reward-img" :src="siteStore.getWebInfo.weixinPay" />
            </span>
          </div>
        </div>
      </el-tooltip>
    </div>

    <!-- 中间文章信息 -->

    <el-card class="article" id="articleBox">
      <el-tag @click="handleClike(article.category.id, '/category')" effect="dark" class="category hand-style">
        {{ article.category.name }}
      </el-tag>
      <h1 class="article-title">
        {{ article.title }}
      </h1>
      <div class="article-desc">
        <div class="article-item">
          <img v-lazy="article.avatar" :key="article.avatar" alt="" />
          <div class="meta">
            <div class="author">
              <span class="link" href="#">{{ article.nickname }}</span>
            </div>
            <div class="item">
              <!-- <span class="text textItem" v-if="article.address">
                <el-icon color="#409eff">
                  <Location />
                </el-icon>
                发布于 {{ article.address }}</span> -->
              <span class="text textItem">
                <el-icon color="#f56c6c">
                  <Clock />
                </el-icon>
                {{ formatDate(article.createTime) }}</span>
              <!-- <span class="text textItem">
                <el-icon color="#67c23a">
                  <ChatDotRound />
                </el-icon>
                {{ article.commentCount }}评论</span> -->
              <!-- <span class="text textItem">
                <i style="font-size: 0.7rem; color: rgb(38, 211, 153)" class="iconfont icon-dianzan1"></i>
                {{ article.likeCount == null ? 0 : article.likeCount }}点赞
              </span> -->
              <!-- <span class="text">
                <el-icon color="#bedd10">
                  <View />
                </el-icon>
                {{ article.quantity }}阅读</span> -->
            </div>
          </div>
        </div>
        <time class="time">
          {{ formatDate(article.createTime, "MM/dd") }}
        </time>
      </div>
      <div class="divider">
        <div class="first"></div>
        <!-- <el-icon>
          <Bicycle />
        </el-icon>
        <div class="second"></div> -->
      </div>
      <!-- 文章内容 -->
      <div style="height: 100%" class="box-article">
        <article class="content" :style="style" ref="articleRef" v-highlight v-html="article.content"></article>
        <div v-if="article.readType != 0 && !serceShow" class="warpper">
          <div class="item-title">
            <i class="el-icon-lock"></i> 该文章部分内容已隐藏
          </div>
          <div class="item-box">
            <span>
              {{ readTypeList[article.readType] }}
            </span>
            <div class="neirong">
              以下内容已隐藏，请{{ readDescList[article.readType] }}后查看
            </div>
            <!-- <el-button v-if="article.readType == 1" @click="checkLikeAndCoomment('请到文章内容下方完成评论')" class="btn"
              type="primary" size="small">立即评论</el-button>
            <el-button v-if="article.readType == 2" @click="checkLikeAndCoomment('请到文章内容左侧完成点赞')" class="btn"
              type="primary" size="small">立即点赞</el-button>
            <el-button v-if="article.readType == 3" @click="dialogVisible = true" class="btn" type="primary"
              size="small">立即验证</el-button> -->
          </div>
        </div>
      </div>

      <!-- <div class="read-duration">
        <div class="duration">
          阅读时长，您已阅读：{{ hour }}时{{ minute }}分{{ second }}秒。
        </div>
      </div> -->

      <!-- 移动端点赞 -->
      <!-- <div class="dianzanBox">
        <div class="dianzan-item">
          <div>
            <span @click="doLike(article.id)">
              <i v-if="article.isLike" class="iconfont icon-dianzan4"></i>
              <i v-else class="iconfont icon-dianzan1"></i>
            </span>
          </div>

          <div v-if="article.likeCount" class="likeCountItem">
            {{ article.likeCount }}人已点赞
          </div>
        </div>
      </div> -->

      <!-- 文章标签和分享 -->
      <div class="tag-share">
        <div>
          <a class="tagBtn hand-style" v-for="item in article.tagList" :key="item.id"
            @click="handleClike(item.id, '/tags')">
            <el-tag type="info" effect="dark">{{ item.name }}</el-tag>
          </a>
        </div>
        <!-- 分享 -->
        <!-- <div class="social-share" id="social-share" @mouseleave="hiddenShareItmes" @mouseenter="showShareItmes">
          <div class="share-item" id="share-item">
            <a href="javascript:;" @click="qqShare" class="social-share-icon icon-qzone">
              <i class="iconfont icon-qqkongjian"></i>
            </a>
            <a href="javascript:;" @click="qqHyShare" class="social-share-icon icon-qq">
              <i class="iconfont icon-QQ"></i>
            </a>
            <a href="javascript:;" @click="weixinShare" class="social-share-icon icon-wechat">
              <i class="iconfont icon-weixin"></i>
            </a>
            <a href="javascript:;" @click="weiboShare" class="social-share-icon icon-weibo">
              <i class="iconfont icon-shejiaotubiao-06"></i>
            </a>
          </div>
          <div style="
              width: 34px;
              display: inline-block;
              line-height: 34px;
              height: 34px;
            ">
            <i class="iconfont icon-fenxiang share hand-style"></i>
          </div>
        </div> -->
      </div>
      <!-- 版权 -->
      <!-- <div class="copyright">
        <div class="copyrightItem">
          <svg-icon name="yuanchuang"></svg-icon>
          <span class="text name">创作类型:</span>
          <span class="text"> {{ article.isOriginal ? "原创" : "转载" }}</span>
        </div>
        <div class="copyrightItem" v-if="article.isOriginal">
          <svg-icon name="copyright"></svg-icon>
          <span class="text name">版权归属:</span>
          <span class="text"> {{ siteStore.getWebInfo.author }}</span>
        </div>
        <div class="copyrightItem" v-else>
          <svg-icon name="zzlink"></svg-icon>
          <span class="text name">转载链接:</span>
          <a :href="article.originalUrl" class="text">
            {{ article.originalUrl }}</a>
        </div>
        <div class="copyrightItem">
          <svg-icon name="link"></svg-icon>
          <span class="text name">本文链接:</span>
          <a href="" class="text"> {{ locationUrl }}</a>
        </div>
        <div class="copyrightItem">
          <svg-icon name="xkxy"></svg-icon>
          <span class="text name">许可协议:</span>
          <span class="text">
            本博客所有文章除特别声明外，均采用
            <a class="text" href="https://creativecommons.org/licenses/by-nc-sa/4.0/">
              CC BY-NC-SA 4.0
            </a>
            许可协议。转载请注明文章出处。
          </span>
        </div>
        <svg-icon class="yuan" name="yuan"></svg-icon>
      </div> -->

      <!-- 评论 -->
      <!-- <div class="comment-mian" id="comment" v-if="siteStore.getWebInfo.openComment">
        <div class="title">
          <svg-icon name="message"></svg-icon>
          评论
          <span style="color: var(--text-color); font-size: 0.8rem">发表评论,来抢沙发</span>
        </div>
        <Comment :articleUserId="article.userId" />
      </div> -->
    </el-card>
    <!-- 右边侧边栏 -->
    <div class="sidebar" v-if="rightShow">
      <div style="position: sticky; top: 70px">
        <!-- <div style="margin-top: 80px">
          <SiteInfo />
        </div> -->
        <div class="directory" @mouseenter="handleDireMousEnter" @mouseleave="handleDireMousLeave" v-if="titles.length">
          <el-card class="directory-item">
            <template #header>
              <div style="color: var(--article-color)">
                <i class="iconfont icon-menu_normal"></i> <span>文档目录</span>
              </div>
            </template>
            <el-scrollbar max-height="500px">
              <ul>
                <li ref="directoryItem" :style="{
                  filter: active != index ? 'blur(1px)' : 'none',
                }" :class="active == index
                      ? 'structure active hand-style'
                      : 'structure hand-style'
                    " v-for="(item, index) in titles" :key="index" @click="handleNodeClick(item.id)">
                  <el-text line-clamp="2" size="large">
                    {{ item.title }}
                  </el-text>
                </li>
              </ul>
            </el-scrollbar>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 公众号扫码验证框 -->
    <el-dialog :lock-scroll="false" title="关注公众号验证" center v-model="dialogVisible">
      <div style="text-align: center">
        <div>
          扫码关注公众号<span style="color: red">【编程 】</span>
        </div>
        <div>回复<span style="color: red">【 验证码 】</span>获取验证码</div>
      </div>
      <el-image class="wxImg" :src="defaultSetting.wxOpenImg">
        <template #error> 加载中<span class="dot">...</span> </template>
      </el-image>

      <el-input wi v-model="code" placeholder="请输入验证码"></el-input>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCheckCode">确 定</el-button>
      </template>
    </el-dialog>

    <image-preview :img="images"></image-preview>
  </div>
</template>
<script setup name="Article">
import Clipboard from "clipboard";
import {
  articleInfo,
  articleLike,
  checkCode,
  followedUser,
  deleteFollowedUser,
} from "@/api";
import { collect, cancelCollect } from "@/api/collect";
import SiteInfo from "@/components/authorInfo/index.vue";
import Comment from "@/components/comment/index.vue";
import imagePreview from "@/components/image-preview/index.vue";

import { useSiteStore } from "@/store/moudel/site.js";
import { useUserStore } from "@/store/moudel/user.js";

const { proxy } = getCurrentInstance();
const siteStore = useSiteStore();
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const article = ref({
  category: {},
  comments: [],
  tagList: [],
});
const rightShow = ref(true);
const code = ref(null);
const style = ref("");
const titles = ref([]);
const images = ref({});
const readTypeList = ref(["", "评论阅读", "点赞阅读", "扫码阅读"]);
const readDescList = ref(["", "评论", "点赞", "扫码回复验证码"]);
const dialogVisible = ref(false);
const active = ref(0);
const locationUrl = ref(window.location.href);
const articleId = ref(route.params.articleId);
const serceShow = ref(0);
const left = ref("0px");
const codes = ref([]);
const timer = ref(null);
const second = ref(0);
const minute = ref(0);
const hour = ref(0);
const articleRef = ref();
const directoryItem = ref();
const fullscreenLoading = ref(true);
const defaultSetting = ref(proxy.$setting);

onMounted(() => {
  const element = document.getElementById("articleBox");
  left.value = element.offsetLeft - 80 + "px";

  // 监听滚动事件
  window.addEventListener("scroll", onScroll, false);
  timer.value = setInterval(() => {
    if (second.value == 60) {
      second.value = 0;
      minute.value++;
    } else {
      second.value++;
    }
    if (minute.value == 60) {
      minute.value = 0;
      hour.value++;
    }
  }, 1000);
});
onBeforeUnmount(() => {
  clearInterval(timer.value);
  window.removeEventListener("scroll", onScroll);
});

//   computed: {
//     isCommentFlag() {
//       return $store.state.isCommentFlag;
//     },
//   },
//   watch: {
//     isCommentFlag: function (newVal, oldVal) {
//       //插入需要在仓库数据变化时做的逻辑处理
//       if (newVal) {
//         checkAfter();
//       }
//     },
//   },

window.addEventListener(
  "resize",
  () => {
    const element = document.getElementById("articleBox");
    if (element) {
      left.value = element.offsetLeft - 80 + "px";
    }
  },
  true
);
articleInfo(articleId.value)
  .then((res) => {
    article.value = res.data;
    serceShow.value = article.value.activeReadType;
    if (article.value.readType != 0 && !serceShow.value) {
      style.value = "max-height: 1200px;overflow: hidden;";
    }

    //处理目录和图片预览
    nextTick(() => {
      //添加图片预览功能
      const imgList = articleRef.value.getElementsByTagName("img");
      for (var i = 0; i < imgList.length; i++) {
        imgList[i].addEventListener("click", function (e) {
          images.value = {
            urls: e.target.currentSrc,
            time: new Date().getTime(),
          };
        });
      }
      // 添加文章生成目录功能
      let nodes = articleRef.value.querySelectorAll("h1,h2,h3,h4,h5,h6");
      for (let i = 0; i < nodes.length; i++) {
        let node = nodes[i];
        let reg = /^H[1-6]{1}$/;
        if (reg.exec(node.tagName)) {
          node.id = "h" + parseInt(node.tagName.substring(1)) + i;
        }
        let toc = {
          id: node.id,
          title: node.innerText,
        };
        titles.value.push(toc);
      }
      //修改代码样式和添加复制按钮
      getCodes();
    });

    //修改标题和关键词
    document.title = article.value.title;
    if (article.value.keywords != null) {
      document
        .querySelector('meta[name="keywords"]')
        .setAttribute("content", article.value.keywords);

      document
        .querySelector('meta[name="description"]')
        .setAttribute("content", article.value.summary);
    }
  })
  .finally(() => (fullscreenLoading.value = false));

//鼠标移入 目录取消滤镜
function handleDireMousEnter() {
  for (let i = 0; i < directoryItem.value.length; i++) {
    directoryItem.value[i].style.filter = "none";
  }
}
//鼠标移出 目录滤镜
function handleDireMousLeave() {
  for (let i = 0; i < directoryItem.value.length; i++) {
    if (active != i) {
      directoryItem.value[i].style.filter = "blur(1px)";
    }
  }
}

//描点跳转
function handleNodeClick(data, event) {
  document.getElementById(data).scrollIntoView({ behavior: "smooth" });
}

//获取所有代码
function getCodes() {
  codes.value = document.querySelectorAll("pre");
  if (codes.value.length > 0) {
    for (let i = 0; i < codes.value.length; i++) {
      if (codes.value[i].offsetHeight != 0) {
        return codeStyleInit();
      } else {
        for (let j = 0; j < codes.value.length; j++) {
          if (codes.value[j].offsetHeight != 0) {
            return codeStyleInit();
          }
        }
        return;
      }
    }
    return;
  }
}

//代码样式初始化
function codeStyleInit() {
  codes.value.forEach((item, index) => {
    let icon =
      `<div class="mac-icon">` +
      `<span class="mac-icon-red"></span>` +
      `<span class="mac-icon-yellow"></span>` +
      `<span class="mac-icon-green"></span>` +
      `<i class="iconfont icon-fuzhi copy-button hand-style"></i>` +
      `</div>`;
    item.insertAdjacentHTML("afterbegin", icon);
    let value = item.lastElementChild.innerText;
    // 获取复制元素
    let copyButton =
      item.firstElementChild.getElementsByClassName("copy-button")[0];
    copyButton.onclick = function () {
      const clipboard = new Clipboard(".copy-button", {
        text: () => value,
      });
      clipboard.on("success", () => {
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        proxy.$modal.msgError("复制失败");
        clipboard.destroy();
      });
    };
  });
}
//qq空间分享
function qqShare() {
  const url = `https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=${window.location.href}&sharesource=qzone&title=${article.title}&summary=${article.title}`;
  window.open(url, "renren-share", "width=490,height=700");
}
//qq好友分享
function qqHyShare() {
  const url = `http://connect.qq.com/widget/shareqq/index.html?url=${window.location.href}&sharesource=qzone&title=${article.title}&summary=${article.title}`;
  window.open(url, "renren-share", "width=490,height=700");
}
//微博分享
function weiboShare() {
  const url = `http://service.weibo.com/share/share.php?url=${window.location.href}&title=${article.title}`;
  window.open(url, "renren-share", "width=490,height=700");
}
//微信分享
function weixinShare() {
  const url = `https://api.pwmqr.com/qrcode/create/?url=${window.location.href}`;
  window.open(url, "renren-share", "width=490,height=700");
}

//显示分享按钮列表
function showShareItmes() {
  document.getElementById("share-item").style.display = "inline-block";
  document.getElementById("social-share").style.backgroundColor =
    "var(--article-share-color)";
}

//隐藏分享按钮列表
function hiddenShareItmes() {
  document.getElementById("share-item").style.display = "none";
  document.getElementById("social-share").style.backgroundColor = "unset";
}

function checkLikeAndCoomment(desc) {
  proxy.$modal.msg(desc);
}
function handleCheckCode() {
  if (!code.value) {
    proxy.$modal.msgError("验证码不能为空！");
    return;
  }
  checkCode(code.value)
    .then((res) => {
      proxy.$modal.msgSuccess("验证成功");
      checkAfter();
    })
    .catch((err) => { });
}
function checkAfter() {
  dialogVisible.value = false;
  style.value = "";
  serceShow.value = true;
}

//校验是否登录
function handleValidateLogin() {
  if (!userStore.getUserInfo) {
    userStore.setLoginFlag(true);
    return;
  }
}
//滚动
function onScroll() {
  const scrollTop =
    document.documentElement.scrollTop || document.body.scrollTop;
  for (let i = 0; i < titles.value.length; i++) {
    //获取小标题距离屏幕顶端的距离
    var offsetTop = document.getElementById(titles.value[i].id).offsetTop;
    //根据条件做出判断，我这里是当小标题和屏幕顶端的距离小于300且没有被标明在读时，就将其背景颜色改变。
    if (offsetTop - scrollTop < 80 && offsetTop - scrollTop > 0) {
      active.value = i;
    }
  }
}

//沉浸式阅读
function handleImmerse() {
  rightShow.value = !rightShow.value;
  window.setTimeout(() => {
    const element = document.getElementById("articleBox");
    left.value = element.offsetLeft - 80 + "px";
  }, 10);
}

//收藏或取消
function doCollect() {
  handleValidateLogin();
  let id = article.value.id;
  if (article.value.isCollect) {
    cancelCollect(id).then((res) => {
      article.value.collectCount--;
      article.value.isCollect = 0;
      proxy.$modal.msgSuccess("收藏成功");
    });
  } else {
    collect(id).then((res) => {
      article.value.collectCount++;
      article.value.isCollect = 1;
      proxy.$modal.msg("取消收藏成功");
    });
  }
}

//带参数跳转页面
function handleClike(id, path) {
  router.push({ path: path, query: { id: id } });
}

//格式化时间
function formatDate(value, args) {
  var dt = new Date(value);
  let year = dt.getFullYear();
  let month = (dt.getMonth() + 1).toString().padStart(2, "0");
  let date = dt.getDate().toString().padStart(2, "0");
  if (args === "MM/dd") {
    return `${month}/${date}`;
  }
  return `${year}-${month}-${date}`;
}
//点赞
function doLike(articleId) {
  handleValidateLogin();
  articleLike(articleId)
    .then((res) => {
      if (article.value.isLike) {
        article.value.likeCount--;
        article.value.isLike = false;

        proxy.$modal.msg("取消点赞");
      } else {
        article.value.likeCount++;
        article.value.isLike = true;
        if (article.value.readType == 2) {
          checkAfter();
        }
        proxy.$modal.msgSuccess("点赞成功");
      }
    })
    .catch((err) => { });
}
</script>
<style lang="scss" scoped>
.article-container {
  padding: 10px;

  .read-duration {
    border-left: 5px solid #50bfff;
    background-color: var(--tips-backgroud-color);
    color: #bf5dd8;
    padding: 10px 0;
    margin-top: 15px;
    padding-left: 10px;
    border-top-right-radius: 5px;
    border-bottom-right-radius: 5px;
  }

  @media screen and (max-width: 1118px) {
    .article {
      width: 100%;

      .article-desc {
        .time {
          display: none;
        }
      }

      .dianzanBox {
        text-align: center;
        margin-top: 15px;

        .dianzan-item {
          i {
            font-size: 1.5rem;
          }

          .likeCountItem {
            color: var(--text-color);
            font-size: 12px;
          }

          svg {
            width: 30px;
            height: 30px;
            vertical-align: -10px;
          }
        }
      }
    }

    .left-sidbarnav,
    .sidebar {
      display: none;
    }
  }

  @media screen and (min-width: 1119px) {
    ::v-deep(.el-dialog) {
      width: 24%;
      border-radius: 10px;
    }

    .article {
      width: 50%;

      .dianzanBox {
        display: none;
      }

      .article-desc {
        .time {
          font-size: 32px;
          line-height: 42px;
          color: orange;
          text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
          position: absolute;
          right: 0;
        }
      }
    }

    .left-sidbarnav {
      position: fixed;
      top: 150px;
      z-index: 999;

      .left-item {
        margin-bottom: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        text-align: center;
        line-height: 50px;
        background-color: var(--background-color);
        position: relative;
        color: var(--text-color);

        &:hover {
          span {
            color: var(--theme-color);

            & {
              background-color: rgba(25, 153, 153, 0.2);
            }
          }
        }

        i,
        .el-icon {
          font-size: 20px;
        }

        .el-icon {
          vertical-align: -2px;
        }

        .like-count {
          color: #fff;
          display: inline-block;
          width: 20px;
          height: 20px;
          border-radius: 50%;
          position: absolute;
          line-height: 20px;
          background-color: var(--theme-color);
          top: -8px;
        }

        .rewardItem {
          position: absolute;
          bottom: -150px;
          left: 52px;
          margin: 0;
          padding: 0 0 15px;
          background-color: #5956563e;
          border-radius: 5px;
          display: none;

          .reward-img {
            margin-left: 5px;
            margin-right: 5px;
            width: 275px;
            height: 300px;
            margin-top: 12px;
          }
        }
      }

      .rewardMain {
        &:hover {
          .rewardItem {
            display: flex;
            animation: left-in 1s ease;

            @keyframes left-in {
              0% {
                transform: translateY(-50%);
              }

              100% {
                transform: translateX(0);
              }
            }
          }
        }
      }
    }

    .sidebar {
      margin-left: 20px;
      width: 20%;

      .directory {
        margin-top: 10px;

        .directory-item {
          background-color: var(--background-color);

          ul {
            margin-top: 8px;
            list-style: none;
            padding: 0 10px;

            &::-webkit-scrollbar {
              display: none;
            }
          }

          ::v-deep(.el-card__header) {
            padding: 15px 20px;
            border-bottom: 2px solid var(--border-line);
          }

          .title {
            display: flex;

            i {
              font-size: 20px;
              margin-right: 10px;
            }

            ::v-deep(.toc-title) {
              color: var(--article-color) !important;
            }
          }

          .structure {
            color: var(--article-color);
            padding: 5px 0;
            padding-left: 20px;
            margin-bottom: 10px;
          }

          .active {
            font-size: 1.2rem;
            transition: all 0.25s ease-in-out;
          }

          .active,
          .structure:hover {
            span {
              color: var(--theme-color);

              & {
                border-radius: 5px;
                background-color: var(--article-structure-backcolor);
              }
            }

          }
        }
      }
    }
  }

  .article {
    background-color: var(--background-color);
    padding: 20px;
    height: 100%;
    margin-top: 80px;

    .category {
      border-radius: 3px;
      transition: transform 0.35s;
      height: 30px;
      line-height: 30px;

      &:hover {
        transform: translateY(-5px);
      }
    }

    .article-title {
      font-size: 24px;
      color: var(--article-color);
      text-align: center;
      padding-top: 20px;
      margin-bottom: 15px;
      word-break: break-word;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
      font-weight: 500;

      svg {
        width: 50px;
        height: 50px;
        vertical-align: -10px;
      }
    }

    .article-desc {
      height: 50px;
      // border-bottom: 1px solid var(--border-line);
      //  margin-bottom: 15px;
      position: relative;
      display: flex;
      align-items: center;
      // padding-bottom: 15px;

      // &::after {
      //   content: "";
      //   position: absolute;
      //   bottom: -1.5px;
      //   left: 0;
      //   width: 80px;
      //   height: 3px;
      //   border-radius: 1.5px;
      //   background: var(--theme-color);
      // }

      .article-item {
        display: flex;

        img {
          width: 30px;
          height: 30px;
          border-radius: 50%;
          margin-right: 10px;
          padding: 3px;
          -o-object-fit: cover;
          object-fit: cover;
          background: var(--border-line);
          border: 1px solid #dcdfe6;
        }

        .meta {
          display: flex;
          flex-direction: column;
          height: 35px;
          justify-content: space-between;
          font-size: 0.9rem;

          .link {
            font-weight: 500;
            color: var(--theme-color);
          }

          .item {
            color: #909399;
            line-height: 16px;
            display: flex;
            align-items: center;
            flex-wrap: wrap;

            .text {
              display: flex;
              align-items: center;

              .el-icon,
              i {
                margin-right: 5px;
              }
            }

            .textItem::after {
              content: "·";
              margin: 0 5px;
            }
          }
        }
      }
    }

    .divider {
      width: 100%;
      height: 2px;
      margin: 20px 0;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;

      .first {
        -webkit-box-flex: 2;
        -ms-flex: 2;
        flex: 2;
        background: #409eff;
      }

      ::v-deep(.el-icon) {
        margin: -16px -10px;
        color: #67c23a;
        font-size: 20px;
      }

      .second {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        background: #e6a23c;
      }
    }

    .tips {
      border-left: 5px solid #50bfff;
      background-color: var(--tips-backgroud-color);
      color: #888;
      border-radius: 5px;
      padding: 10px;
      animation: overdue 2s ease-in-out;

      @keyframes overdue {
        0% {
          -webkit-clip-path: circle(0 at 0 0);
          clip-path: circle(0 at 0 0);
        }

        100% {
          -webkit-clip-path: circle(100%);
          clip-path: circle(100%);
        }
      }

      i {
        color: #50bfff;
        margin-right: 5px;
      }
    }

    .box-article {
      .warpper {
        background: var(--article-srect-background);
        position: relative;
        height: 210px;
        padding: 5px;

        &::before {
          content: "";
          position: absolute;
          top: -80px;
          left: 0;
          width: 100%;
          height: 80px;
          z-index: 2;
          background: linear-gradient(180deg, rgba(55, 55, 55, 0), #ccc);
        }

        .item-title {
          color: #fff;
        }

        .item-box {
          border-radius: 10px;
          background-color: var(--background-color);
          height: 150px;
          margin-left: 10px;
          margin-right: 10px;
          margin-top: 10px;
          margin-bottom: 10px;
          overflow: hidden;

          span {
            background: linear-gradient(135deg, #ff74cd 10%, #ec7d0b);
            border-top-left-radius: 10px;
            border-bottom-right-radius: 10px;
            padding: 5px;
            font-size: 0.9rem;
            color: #fff;
          }

          .neirong {
            text-align: center;
            margin-top: 15px;
            color: var(--text-color);
            font-size: 0.9rem;
          }

          .btn {
            margin: 0 auto;
            display: block;
            margin-top: 20px;
          }
        }
      }
    }

    ::v-deep(.content) {
      color: var(--article-color);
      line-height: 30px;
      margin-top: 10px;
      padding: 10px;

      .hljs-center {
        text-align: center;
      }

      blockquote {
        position: relative;
        padding: 0 10px;
        color: #6a737d;
        border-left: 0.25em solid #dfe2e5;
        margin: 20px 0;
      }

      h1,
      h2,
      h3,
      h4,
      h5,
      h6 {
        margin: 10px 0;
      }

      table {
        background-color: var(--article-table-back-color);

        td {
          border: 1px solid var(--article-table-border-color);
          padding: 5px;
        }

        tr {
          background-color: var(--article-table-border-color);
          transition: all 0.3s ease-in-out;

          &:hover {
            background-color: var(--article-table-back-color);
            border: none;
          }
        }

        th {
          background-color: var(--article-table-back-color);
        }
      }

      p {
        a {
          text-decoration: none;
          color: #7bc549;

          &::after {
            content: "🔗";
          }
        }
      }

      code {
        vertical-align: middle;
        word-break: break-word !important;
        border-radius: 2px !important;
        overflow-x: auto !important;
        background-color: #fff5f5;
        color: #ff502c;
        font-size: 0.87em !important;
        padding: 0.065em 0.4em !important;
      }

      ol {
        margin-left: 20px;
      }

      pre {
        opacity: 1 !important;
        margin: 10px 0;
        color: #f8f8f2 !important;
        overflow: hidden;
        box-shadow: rgba(0, 0, 0, 0.55) 0px 2px 10px;
        border-radius: 5px;

        code {
          line-height: 20px !important;
          font-size: 16px !important;
          vertical-align: top;
          padding: 10px !important;
          border-bottom-left-radius: 5px !important;
          border-bottom-right-radius: 5px !important;
          background-color: #303133 !important;
          color: #c0c4cc !important;
          width: -webkit-fill-available;
          width: -moz-available;
          display: inline-block;
        }
      }

      img {
        max-width: 100%;
        margin: 15px 0;
        border-radius: 5px !important;
        transition: box-shadow 0.35s, transform 0.35s;
        cursor: pointer;
        max-height: 500px;

        &:hover {
          box-shadow: 5px 10px 5px rgba(0, 0, 0, 0.2);
          transform: translateY(-10px);
        }
      }

      ul {
        margin-left: 20px;
      }
    }

    .tag-share {
      display: flex;
      margin-right: 5px;
      align-items: center;
      padding-bottom: 15px;
      margin-bottom: 20px;
      position: relative;
      border-bottom: 1px solid var(--border-line);
      margin-top: 20px;
      justify-content: space-between;

      .tagBtn {
        margin-right: 5px;
      }

      .social-share {
        position: relative;
        border-top-left-radius: 25px;
        border-bottom-left-radius: 25px;
        height: 100%;
        display: flex;
        align-items: center;

        &:hover .share {
          transform: rotate(360deg);
        }

        .share-item {
          text-align: left !important;
          width: auto;
          display: none;
          margin-right: 15px;
          transition: display 0.3s ease-in-out;
        }

        .share {
          display: inline-block;
          font-size: 18px;
          width: 100%;
          height: 100%;
          font-weight: 700;
          text-align: center;
          color: #fff;
          background: #99f;
          border-radius: 50%;
          transition: all 0.3s;
        }

        .social-share-icon {
          margin-left: 10px;
          display: inline-block;
          width: 30px;
          height: 30px;
          font-size: 20px;
          border-radius: 50%;
          line-height: 30px;
          border: 1px solid #666;
          color: #666;
          text-align: center;
          vertical-align: middle;
          text-decoration: none;

          i {
            font-size: 25px;
          }
        }

        .icon-qzone {
          border-color: #fdbe3d;
          color: #fdbe3d;

          &:hover {
            background-color: #fdbe3d;

            i {
              color: white;
            }
          }
        }

        .icon-wechat {
          color: #7bc549 !important;
          border-color: #7bc549;

          &:hover {
            background-color: #7bc549;

            i {
              color: white;
            }
          }
        }

        .icon-weibo {
          color: #ff763b !important;
          border-color: #ff763b;

          &:hover {
            background-color: #ff763b;

            i {
              color: white;
            }
          }
        }

        .icon-qq {
          color: #56b6e7 !important;
          border-color: #56b6e7;

          &:hover {
            background-color: #56b6e7;

            i {
              color: white;
            }
          }
        }
      }
    }

    .wechatImg {
      img {
        width: 100%;
      }
    }

    .copyright {
      background-color: var(--text-color3);
      border-radius: 5px;
      width: 100%;
      min-height: 130px;
      position: relative;
      border: 1px dashed var(--theme-color);
      margin-top: 20px;

      &::before {
        content: "声明";
        background-image: linear-gradient(to right, #ffcc99, #ff99cc);
        width: 30%;
        padding: 5px;
        border-radius: 999px;
        position: absolute;
        top: -18px;
        left: 35%;
        text-align: center;
        border: 1px dashed var(--theme-color);
        font-weight: 700;
      }

      .copyrightItem {
        padding: 10px 10px;
        height: 100%;
        line-height: 26px;

        svg {
          width: 18px;
          height: 18px;
          margin-right: 3px;
          vertical-align: -4px;
        }

        .text {
          color: #909399;
          font-size: 14px;
          margin-left: 8px;
          text-decoration: none;
        }

        .name {
          color: var(--theme-color);
          font-weight: 700;
        }

        a:hover {
          color: var(--theme-color);
        }
      }

      .yuan {
        width: 20px;
        height: 20px;
        position: absolute;
        top: 10px;
        right: 10px;
      }
    }

    .comment-mian {
      margin-top: 30px;

      .title {
        font-weight: 700;
        font-size: 20px;
        margin-top: 20px;
        color: var(--article-color);

        svg {
          width: 20px;
          height: 20px;
        }
      }
    }
  }
}

.wxImg {
  display: block;
  margin: 0 auto;
  width: 50%;
  height: 50%;
  margin-bottom: 15px;
}
</style>

<style>
.hljs {
  border-bottom-left-radius: 5px !important;
  border-bottom-right-radius: 5px !important;
}

.mac-icon {
  height: 15px !important;
  display: flex;
  align-items: center;
  border: 1px solid #272822 !important;
  background-color: #272822 !important;
  padding: 8px !important;
  border-top-left-radius: 5px !important;
  border-top-right-radius: 5px !important;
}

.mac-icon>span {
  display: inline-block !important;
  letter-spacing: 5px !important;
  word-spacing: 5px !important;
  width: 10px !important;
  height: 10px !important;
  border-radius: 8px !important;
}

.mac-icon-red {
  background-color: red !important;
}

.mac-icon-yellow {
  margin-left: 10px;
  background-color: yellow !important;
}

.mac-icon-green {
  margin-left: 10px;
  background-color: green !important;
}

.mac-icon-lang {
  width: 50px !important;
  padding-left: 10px !important;
  font-size: 16px !important;
  vertical-align: top !important;
}

.copy-button {
  border-radius: 3px;
  padding: 5px !important;
  color: #ffffff !important;
  font-size: 1rem;
  margin-left: auto;
}

.copy-button:hover {
  background-color: black !important;
}

.line-num-box {
  display: inline-block !important;
  color: #ccc !important;
  border-right: 2px solid #fff !important;
  line-height: 20px !important;
  font-size: 16px !important;
  text-align: right !important;
  padding-left: 10px !important;
  padding-right: 10px !important;
}
</style>
