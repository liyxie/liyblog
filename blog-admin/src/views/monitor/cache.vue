<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card style="margin-bottom: 10px">
          <template #header><span>基本信息</span></template>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">Redis版本</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.redis_version }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">运行模式</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{
                        cache.info.redis_mode == "standalone" ? "单机" : "集群"
                      }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">端口</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.tcp_port }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">客户端数</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.connected_clients }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">运行时间(天)</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.uptime_in_days }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">使用内存</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.used_memory_human }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">使用CPU</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{
                        parseFloat(cache.info.used_cpu_user_children).toFixed(2)
                      }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">内存配置</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.maxmemory_human }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">AOF是否开启</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.aof_enabled == "0" ? "否" : "是" }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">RDB是否成功</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.rdb_last_bgsave_status }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">Key数量</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.dbSize">
                      {{ cache.dbSize }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">网络入口/出口</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="cache.info">
                      {{ cache.info.instantaneous_input_kbps }}kps/{{
                        cache.info.instantaneous_output_kbps
                      }}kps
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <!-- <el-col :span="12" class="card-box">
        <el-card>
          <template #header><span>命令统计</span></template>
          <div
            style="margin: 0 auto; text-align: center"
            class="el-table el-table--enable-row-hover el-table--medium"
          >
            <div ref="commandstatsRef" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card style="margin-left: 10px">
          <template #header>
            <span>内存信息</span>
          </template>
          <div style="margin: 0 auto; text-align: center">
            <div ref="usedmemoryRef" style="height: 420px" />
          </div>
        </el-card>
      </el-col> -->
    </el-row>

    <el-card shadow="never" class="table-container">
      <template #header>数据列表</template>
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="tableData"
        highlight-current-row
        stripe
        fit
        max-height="600px"
      >
        <el-table-column label="键" align="center" prop="key" />
        <el-table-column
          label="值"
          align="center"
          prop="value"
          :show-overflow-tooltip="true"
        >
          <template #default="scope">
            <div v-if="scope.row.value">{{ scope.row.value }}</div>
            <div class="not-show" @click="getKeyValue(scope.row)" v-else>
              点击加载
            </div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              link
              v-hasPerm="['system:cache:delete']"
              @click="handleDelete(scope.row.key)"
            >
              <i-ep-delete />删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="handleQuery"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { getCacheInfo, getCacheList, getValue, deleteCache } from "@/api/cache";
import * as echarts from "echarts";

defineOptions({
  name: "Cache",
  inheritAttrs: false,
});

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const loading = ref(false);

const commandstats = ref();
const usedmemory = ref();
const cache = ref({});

const tableData = ref([]);
const total = ref(0);

const commandstatsRef = ref();
const usedmemoryRef = ref();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getCacheList(queryParams)
    .then(({ data }: any) => {
      tableData.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 根据键获取值 */
function getKeyValue(item: any) {
  loading.value = true;
  getValue(item.key)
    .then(({ data }: any) => {
      item.value = data;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 删除 */
function handleDelete(key: string) {
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteCache(key)
      .then(() => {
        ElMessage.success("删除成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

/** 查缓存询信息 */
function getInfo() {
  getCacheInfo().then((response: any) => {
    cache.value = response.data;
    commandstats.value = echarts.init(commandstatsRef.value, "macarons");
    commandstats.value.setOption({
      tooltip: {
        trigger: "item",
        formatter: "{a} <br/>{b} : {c} ({d}%)",
      },
      series: [
        {
          name: "命令",
          type: "pie",
          roseType: "radius",
          radius: [15, 95],
          center: ["50%", "38%"],
          data: response.data.commandStats,
          animationEasing: "cubicInOut",
          animationDuration: 1000,
        },
      ],
    });
    usedmemory.value = echarts.init(usedmemoryRef.value, "macarons");
    usedmemory.value.setOption({
      tooltip: {
        formatter: "{b} <br/>{a} : " + cache.value.info.used_memory_human,
      },
      series: [
        {
          name: "峰值",
          type: "gauge",
          min: 0,
          max: 1000,
          detail: {
            formatter: cache.value.info.used_memory_human,
          },
          data: [
            {
              value: parseFloat(cache.value.info.used_memory_human),
              name: "内存消耗",
            },
          ],
        },
      ],
    });
  });
}

onMounted(() => {
  handleQuery();
  getInfo();
});
</script>

<style>
.not-show {
  background-color: #eee;
  cursor: pointer;
  padding: 5px 10px;
}
</style>
