<template>
  <div class="container" v-sy-loading="fullscreenLoading">
    <div class="tag-wapper">
      <div class="left">
        <div class="tag-box">
          <div class="tag-title">
            <svg-icon name="tag"></svg-icon> 标签 ({{ tagList.length }})
          </div>
          <div class="tag-list">
            <a
              ref="tagRef"
              :style="{ fontSize: tag.font }"
              :class="
                handleChoose(tag, index)
                  ? 'tag-option hand-style active'
                  : 'tag-option hand-style'
              "
              @click="handleClick(tag.id, index)"
              v-for="(tag, index) in tagList"
              :key="index"
            >
              {{ tag.name }} <span class="num">{{ tag.articleNum }}</span>
            </a>
          </div>
        </div>

        <div class="article-list" v-if="articleList.length">
          <div
            class="article-item box-shadow-top wow pulse"
            v-for="(article, index) in articleList"
            :key="index"
          >
            <div class="article-cover">
              <img v-lazy="article.avatar" :key="article.avatar" alt="" />
            </div>
            <div class="article-right">
              <router-link :to="'/article/' + article.id">
                <h4>{{ article.title }}</h4>
              </router-link>
              <div class="article-meta">
                <el-avatar
                  class="userAvatar"
                  :src="siteStore.getWebInfo.authorAvatar"
                ></el-avatar>
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="文章分类"
                  placement="top"
                >
                  <el-tag
                    class="hand-style"
                    @click="handleNatigation(article.categoryId, '/category')"
                  >
                    <i class="el-icon-folder-opened"></i>
                    {{ article.categoryName }}
                  </el-tag>
                </el-tooltip>
                <el-icon><Clock /></el-icon> {{ article.formatCreateTime }}
              </div>
            </div>
          </div>
          <!-- 分页按钮 -->
          <div>
            <sy-pagination
              :pageNo="pageData.pageNo"
              :pages="pages"
              @changePage="handlePage"
            />
          </div>
        </div>
        <sy-empty v-else message="此标签下暂无发布文章" />
      </div>
      <div class="right">
        <AuthorInfo style="position: sticky; top: 60px" />
      </div>
    </div>
  </div>
</template>

<script setup name="tag">
import WOW from "wow.js";
import "wow.js/css/libs/animate.css";

import AuthorInfo from "@/components/authorInfo/index.vue";
import { listArticle, listTagAll } from "@/api";
import { useSiteStore } from "@/store/moudel/site.js";

const siteStore = useSiteStore();
const route = useRoute();
const router = useRouter();
const tagList = ref([]);
const articleList = ref([]);
const pages = ref(0);
const lastIndex = ref(null);
const pageData = ref({
  pageNo: 1,
  pageSize: 8,
  tagId: route.query.id,
});
const tagRef = ref();
const fullscreenLoading = ref(false);

watch(route, (newX) => {
  pageData.value.pageNo = 1;
  pageData.value.tagId = newX.query.id
  articleList.value = []
  getArticleList()
});

/**跳转 */
function handleNatigation(id, path) {
  router.push({ path: path, query: { id: id } });
}

/**选中标签 */
function handleChoose(item) {
  return item.id == pageData.value.tagId;
}

/**点击标签 */
function handleClick(id, index) {
  if (index == lastIndex.value) {
    return;
  }
  fullscreenLoading.value = true;
  tagRef.value[index].className = "tag-option hand-style active";
  if (lastIndex.value != null) {
    tagRef.value[lastIndex.value].className = "tag-option hand-style ";
  }
  lastIndex.value = index;
  pageData.value.pageNo = 1;
  pageData.value.tagId = id;
  articleList.value = [];
  getArticleList();
}

/** 分页 */
function handlePage(val) {
  fullscreenLoading.value = true;
  pageData.value.pageNo = val;
  getArticleList();
}

/** 获取所有标签 */
function getTagList() {
  listTagAll().then((res) => {
    tagList.value = res.data;
    for (var i = 0; i < tagList.value.length; i++) {
      tagList.value[i].font = Math.floor(Math.random() * 10) + 10 + "px";
    }
    if (pageData.value.tagId == null) {
      pageData.value.tagId = tagList.value[0].id
    }
    getArticleList();
  });
}

/** 根据标签获取文章 */
function getArticleList() {
  listArticle(pageData.value)
    .then((res) => {
      articleList.value.push(...res.data.records);
      pages.value = res.data.pages;
    })
    .finally(() => (fullscreenLoading.value = false));
}

onMounted(() => {
  getTagList();
  const wow = new WOW({
    boxClass: "wow", // animated element css class (default is wow)
    animateClass: "animated", // animation css class (default is animated)
    offset: 0, // distance to the element when triggering the animation (default is 0)
    mobile: true, // trigger animations on mobile devices (default is true)
    live: true, // act on asynchronously loaded content (default is true)
    callback: function (box) {
      // the callback is fired every time an animation is started
      // the argument that is passed in is the DOM node being animated
    },
    scrollContainer: null, // optional scroll container selector, otherwise use window
  });
  wow.init();
});
</script>

<style lang="scss" scoped>
.tag-wapper {
  margin-top: 80px;

  display: flex;
  justify-content: space-between;

  @media screen and (max-width: 1118px) {
    width: 100%;

    .left {
      width: 100%;

      .article-cover {
        display: none;
      }
    }

    .right {
      display: none;
    }
  }

  @media screen and (min-width: 1119px) {
    width: 65%;

    .left {
      width: 70%;

      .article-cover {
        width: 150px;
        height: 80px;
      }
    }

    .right {
      width: 30%;
    }
  }

  .left {
    color: var(--text-color);
    margin-right: 15px;

    .tag-box {
      background-color: var(--background-color);
      padding: 15px;
      border-radius: 5px;

      .tag-title {
        font-size: 1.2rem;
        margin-bottom: 20px;
        margin-top: 10px;

        svg {
          width: 20px;
          height: 20px;
          vertical-align: -5px;
        }
      }

      .tag-list {
        .tag-option {
          display: inline-block;
          border: 1px solid var(--border-line);
          border-radius: 5px;
          margin-right: 10px;
          padding: 5px;
          margin-bottom: 10px;

          &:hover {
            background-color: rgb(171, 214, 214);
          }

          .num {
            display: inline-block;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            background-color: #66b1ff;
            text-align: center;
            line-height: 20px;
            color: white;
            font-size: 12px;
          }
        }

        .active {
          background-color: rgb(171, 214, 214);
        }
      }
    }

    .article-list {
      margin-top: 20px;

      .article-item {
        padding: 10px;
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        background-color: var(--background-color);

        &:last-child {
          border-bottom: none;
        }

        .article-cover {
          border-radius: 5px;

          img {
            width: 100%;
            height: 100%;
            border-radius: 5px;
          }
        }

        .article-right {
          display: flex;
          flex-direction: column;
          margin-left: 10px;

          a {
            text-decoration: none;
            color: var(--text-color);
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;

            &:hover {
              color: var(--theme-color);
            }
          }
        }

        .article-meta {
          margin-top: 15px;
          display: flex;
          align-items: center;

          .userAvatar {
            margin-right: 10px;
          }

          i {
            margin: 0 5px;
          }
        }
      }
    }
  }
}
</style>
