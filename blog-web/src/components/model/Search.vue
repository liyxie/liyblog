<template>
  <div>
    <el-dialog title="搜索文章" v-model="dialogFormVisible" class="custom-dialog-width">
      <div>
        <el-input v-model="pageData.keyword" @input="inputChage" placeholder="请输入"></el-input>
      </div>

      <div class="tagBox" v-if="!list.length">
        <div class="tag-title">标签搜索</div>
        <div>
          <span @click="handleToTag(tag.id)" :style="{ backgroundColor: `${randomColor()}` }"
            class="tag-item hand-style" v-for="(tag, index) in tagList" :key="index">{{ tag.name }}</span>
        </div>
      </div>
      <el-scrollbar :height="list.length ? '500px' : '0px'">
        <div class="search-article" :key="refKey">
          <div class="item" v-for="(item, index) in list" :key="index">
            <router-link :to="'/article/' + item.id">
              <a class="xiahuaxian article-title" v-html="item.title"></a>
            </router-link>
            <div class="summary">
              <el-text line-clamp="2" v-html="item.summary" />
            </div>
          </div>
          <!-- 分页按钮 -->
          <sy-pagination v-show="list.length" :pageNo="pageData.pageNo" :pages="pages" @changePage="handlePage" />
        </div>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script setup name="SearchModel">
import { searchArticle, listTagAll } from "@/api";
import { useSiteStore } from "@/store/moudel/site.js";

let timers = null;

const router = useRouter();
const siteStore = useSiteStore();
const pageData = ref({
  pageNo: 1,
  pageSize: 10,
  keyword: "",
});
const pages = ref(0);
const list = ref([]);
const tagList = ref([]);
const refKey = ref(0);

const dialogFormVisible = computed({
  get() {
    return siteStore.getSearchDialogVisible;
  },
  set(value) {
    list.value = [];
    pageData.value.keyword = "";
    siteStore.setSearchDialogVisible(value);
  },
});

function handleToTag(id) {
  siteStore.setSearchDialogVisible(false);
  router.push({ path: "/tags", query: { id: id } });
}
function inputChage(event) {
  clearTimeout(timers);
  timers = setTimeout(() => {
    pageData.value.keyword = event.replace(/\s/g, "");
    if (pageData.value.keyword == "") {
      list.value = [];
      return;
    }
    refKey.value = new Date().getTime();
    pageData.value.pageNo = 1;
    searchArticle(pageData.value).then((res) => {
      list.value = res.data.records;
      pages.value = res.data.pages;
    });
  }, 500); //多久时间内执行一次
}
// 分页
function handlePage(val) {
  pageData.value.pageNo = val;
  fetchArticleList();
}
function fetchArticleList() {
  searchArticle(pageData.value).then((res) => {
    list.value.push(...res.data.records);
    pages.value = res.data.pages;
  });
}
function getTagList() {
  listTagAll().then((res) => {
    tagList.value = res.data;
  });
}
function randomColor() {
  // var letters = "0123456789ABCDEF";
  // var color = "#";
  // do {
  //   for (var i = 0; i < 6; i++) {
  //     color += letters[Math.floor(Math.random() * 16)];
  //   }
  // } while (color === "#FFFFFF" || color === "#000000");
  // return color;

  // 随机色相（0-360），固定较低的饱和度和较高的亮度
  const h = Math.floor(Math.random() * 360);
  const s = 60 + Math.floor(Math.random() * 20);  // 饱和度 60-80%
  const l = 75 + Math.floor(Math.random() * 15);  // 亮度 75-90%
  const a = 0.6;  // 透明度 0.6
  return `hsla(${h}, ${s}%, ${l}%, ${a})`;
}

onBeforeUnmount(() => {
  siteStore.setSearchDialogVisible(false);
});

getTagList();
</script>

<style lang="scss" scoped>
::v-deep(.el-dialog) {
  border-radius: 5px !important;

  @media screen and (min-width: 1119px) {
    width: 30% !important;
  }
}


.dividerBox {
  .divider {
    svg {
      // 保留位置过渡（优先级高于动画）
      transition: 
        left 4s cubic-bezier(0.25, 0.46, 0.45, 0.94),
        filter 0.3s ease,
        transform 0.5s ease !important; // 确保优先级

      // 新增运行状态类控制
      &.running {
        animation: 
          frameSwitch 0.3s steps(3) infinite, // 三帧步进动画
          bounce 1s ease-in-out infinite;
      }
    }

    &:hover svg {
      left: 100%;
      // 触发帧动画
      &.running {
        animation-play-state: running;
      }
    }

    // 三帧步进动画
    @keyframes frameSwitch {
      0% { background-position-x: 0; }
      100% { background-position-x: -300%; }
    }
  }
}

.tagBox {
  margin-top: 15px;

  .tag-title {
    margin-bottom: 10px;
    position: relative;
    padding-left: 10px;

    &::before {
      content: "";
      width: 5px;
      height: 100%;
      background-color: #67c23a;
      position: absolute;
      left: 0;
      bottom: 0;
    }
  }
}

.search-article {
  margin-top: 20px;
  // max-height: 400px;
  // /* 垂直滚动 */
  // overflow-y: scroll;

  .item {
    margin-bottom: 20px;

    a {
      text-decoration: none;
      color: var(--article-color);
    }
  }

  .article-title {
    font-size: 1.2rem;
    font-weight: 700;
    margin-bottom: 10px;
    display: inline-block;
    position: relative;
    padding-left: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 90%;

    &::before {
      content: "";
      width: 5px;
      height: 5px;
      border: 5px solid var(--theme-color);
      position: absolute;
      border-radius: 50%;
      left: 0;
      bottom: 3px;
    }

    &:hover {
      color: var(--theme-color);
    }
  }

  .summary {
    padding-left: 20px;
  }
}
</style>
