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
          <a-button icon="redo" @click="resetFilters">Reset</a-button>
        </div>
      </div>
    </a-card>

    <!-- TABLE -->
    <a-card class="table-card" :bordered="false">
      <div class="actions">
        <a-button type="primary" icon="plus" @click="openModal(null)">Add Agent</a-button>
        <a-button 
          type="danger" 
          icon="delete" 
          :disabled="selectedRowKeys.length === 0"
          @click="batchDelete"
        >
          Delete {{ selectedRowKeys.length > 0 ? `(${selectedRowKeys.length})` : '' }}
        </a-button>
        <a-button icon="import" @click="handleImport">
          Import
        </a-button>
        <a-button icon="download" @click="handleExport">
          Export
        </a-button>
      </div>

      <a-table
        :columns="columns"
        :data-source="list"
        :loading="loading"
        :pagination="{
          ...pagination,
          showTotal: (total, range) => `${range[0]}-${range[1]} of ${total}`,
          showSizeChanger: true,
          pageSizeOptions: ['10', '20', '50', '100']
        }"
        :row-selection="rowSelection"
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
      selectedRowKeys: [],

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

  computed: {
    rowSelection() {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: (selectedRowKeys) => {
          this.selectedRowKeys = selectedRowKeys;
        }
      };
    }
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
    },

    resetFilters() {
      this.filters = {
        name: "",
        category: "",
        phone: "",
        address: "",
        companyId: this.$route.query.companyId || ""
      };
      this.pagination.current = 1;
      this.loadData();
    },

    async batchDelete() {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning("Please select at least one agent to delete");
        return;
      }

      this.$confirm({
        title: `Are you sure you want to delete ${this.selectedRowKeys.length} agent(s)?`,
        content: "This action cannot be undone.",
        okText: "Delete",
        okType: "danger",
        cancelText: "Cancel",
        onOk: async () => {
          try {
            const res = await request({
              url: "/lcagent/delete",
              method: "DELETE",
              params: { ids: this.selectedRowKeys.join(",") }
            });

            if (res && res.code === 200) {
              this.$message.success(`Successfully deleted ${this.selectedRowKeys.length} agent(s)`);
              this.selectedRowKeys = [];
              this.loadData();
            } else {
              this.$message.error(res.data.message || "Failed to delete agents");
            }
          } catch (error) {
            console.error("Batch delete failed:", error);
            this.$message.error("Failed to delete agents");
          }
        }
      });
    },

    handleImport() {
      this.$message.info("Import functionality - Coming soon");
      // TODO: Implement import functionality
      // You can use a file upload modal here
    },

    handleExport() {
      try {
        // Simple CSV export
        const headers = ["Name", "Title", "Phone", "Email", "Office Phone", "Category", "Company"];
        const csvContent = [
          headers.join(","),
          ...this.list.map(item => [
            item.name || "",
            item.title || "",
            item.phone || "",
            item.email || "",
            item.officeNum || "",
            item.category || "",
            item.companyId || ""
          ].map(field => `"${field}"`).join(","))
        ].join("\n");

        const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
        const link = document.createElement("a");
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", `agents_${new Date().getTime()}.csv`);
        link.style.visibility = "hidden";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        this.$message.success("Agents exported successfully");
      } catch (error) {
        console.error("Export failed:", error);
        this.$message.error("Failed to export agents");
      }
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

.actions {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}

.filter-actions {
  display: flex;
  gap: 8px;
}
</style>
