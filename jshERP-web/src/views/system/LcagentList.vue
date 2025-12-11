<template>
  <div class="lcagent-page">

    <!-- FILTERS -->
    <a-card class="filter-card" :bordered="false">
      <div class="filter-container">
        <div class="filter-item">
          <a-input v-model="filters.name" placeholder="Search by Name" allowClear>
            <a-icon slot="prefix" type="user" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="filters.category" placeholder="Category" allowClear>
            <a-icon slot="prefix" type="tag" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="filters.phone" placeholder="Phone" allowClear>
            <a-icon slot="prefix" type="phone" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="filters.address" placeholder="Address" allowClear>
            <a-icon slot="prefix" type="environment" />
          </a-input>
        </div>
        <div class="filter-actions">
          <a-button type="primary" icon="search" @click="loadData">Search</a-button>
        </div>
      </div>
    </a-card>

    <!-- TABLE -->
    <a-card class="table-card" :bordered="false">
      <div class="actions">
        <a-button type="primary" icon="plus" @click="openModal(null)">Add Agent</a-button>
      </div>

      <a-table
        :columns="columns"
        :data-source="list"
        :loading="loading"
        :pagination="pagination"
        rowKey="id"
        @change="handleTableChange"
      >

        <!-- LOGO -->
        <template slot="logo" slot-scope="text">
          <img
            v-if="text"
            :src="'data:image/jpeg;base64,' + text"
            style="width: 50px; height: 50px; object-fit: contain;"
          />
        </template>

        <!-- ACTIONS -->
        <template slot="actions" slot-scope="record">
          <a @click="openModal(record)">Edit</a>
          <a-divider type="vertical" />
          <a @click="deleteRecord(record.id)" style="color: red;">Delete</a>
        </template>

      </a-table>
    </a-card>

    <!-- MODAL -->
    <LcagentModal ref="modal" @saved="loadData" />
  </div>
</template>

<script>
import LcagentModal from "./modules/LcagentModal.vue";
import { axios as request } from "@/utils/request";

export default {
  name: "LcagentList",
  components: { LcagentModal },

  data() {
    return {
      loading: false,
      list: [],

      filters: {
        name: "",
        category: "",
        phone: "",
        address: "",
        companyId: this.$route.query.companyId || ""
      },

      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },

      columns: [
        { title: "Name", dataIndex: "name" },
        { title: "Category", dataIndex: "category" },
        { title: "Phone", dataIndex: "phone" },
        { title: "Address", dataIndex: "address" },

        {
          title: "Logo",
          dataIndex: "logo",
          scopedSlots: { customRender: "logo" }
        },

        {
          title: "Actions",
          width: 120,
          scopedSlots: { customRender: "actions" }
        }
      ]
    };
  },

  mounted() {
    this.loadData();
  },

  methods: {
    async loadData() {
      this.loading = true;

      const params = {
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
        ...this.filters
      };

      try {
        const res = await request({
          url: "/lcagent/select",
          method: "get",
          params
        });

        if (res && res.data !== undefined) {
          this.list = res.data || [];
          this.pagination.total = res.count || 0;
        } else {
          this.list = [];
          this.pagination.total = 0;
          this.$message.warning("No data returned from server");
        }
      } catch (e) {
        console.error("Error loading lcagent data:", e);
        this.list = [];
        this.pagination.total = 0;
        let errorMsg = "Failed to load agent data. Please try again.";
        if (e.response && e.response.data && e.response.data.message) {
          errorMsg = e.response.data.message;
        } else if (e.message) {
          errorMsg = e.message;
        }
        this.$message.error(errorMsg);
      } finally {
        this.loading = false;
      }
    },

    handleTableChange(pagination) {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.loadData();
    },

    openModal(record) {
      this.$refs.modal.open(record);
    },

    deleteRecord(id) {
      this.$confirm({
        title: "Are you sure you want to delete this agent?",
        okType: "danger",
        onOk: async () => {
          await request({
            url: `/lcagent/delete/${id}`, // prefix if needed
            method: "delete"
          });
          this.loadData();
        }
      });
    }
  }
};
</script>

<style scoped lang="less">
.lcagent-page {
  padding: 24px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.filter-card {
  margin-bottom: 24px;
  border-radius: 4px;
}
/* Use deep selector for card body padding if needed */
.filter-card /deep/ .ant-card-body {
  padding: 24px;
}

.filter-container {
  display: flex !important;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  width: 100%;
}

.filter-item {
  flex: 1;
  min-width: 0;
  margin-right: 16px;
}

.filter-actions {
  flex: 0 0 auto;
}
</style>
