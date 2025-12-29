<template>
  <div class="lcproject-page">

    <!-- FILTERS -->
    <a-card class="filter-card" :bordered="false">
      <div class="filter-container">
        <div class="filter-item">
          <a-input v-model="filters.name" placeholder="Search by Project Name" allowClear>
            <a-icon slot="prefix" type="project" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="filters.agency" placeholder="Agency" allowClear>
            <a-icon slot="prefix" type="team" />
          </a-input>
        </div>
        <div class="filter-item status-filter-wrapper">
          <a-icon type="flag" class="prefix-icon" />
          <a-select v-model="filters.status" placeholder="Status" allowClear style="width: 100%" class="select-with-prefix">
            <a-select-option value="active">Active</a-select-option>
            <a-select-option value="completed">Completed</a-select-option>
            <a-select-option value="on-hold">On Hold</a-select-option>
            <a-select-option value="cancelled">Cancelled</a-select-option>
          </a-select>
        </div>
        <div class="filter-item">
          <a-input v-model="filters.location" placeholder="Location" allowClear>
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
        <a-button type="primary" icon="plus" @click="openModal(null)">Add Project</a-button>
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

        <!-- NAME LINK -->
        <template slot="name" slot-scope="text, record">
          <a @click="handleDetail(record)">{{ text }}</a>
        </template>

        <!-- ACTIONS -->
        <template slot="actions" slot-scope="record">
          <a @click="openModal(record)">Edit</a>
          <a-divider type="vertical" />
          <a @click="deleteRecord(record.id)" style="color: red;">Delete</a>
        </template>

        <!-- STATUS -->
        <template slot="status" slot-scope="text">
          <a-tag :color="getStatusColor(text)">{{ text }}</a-tag>
        </template>

      </a-table>
    </a-card>

    <!-- MODAL -->
    <LcprojectModal ref="modal" @saved="loadData" />
    <import-file-modal ref="modalImportForm" @ok="modalFormOk"></import-file-modal>
  </div>
</template>

<!-- f r o m 7 5  2 7 1  8 9 2 0 -->
<script>
import LcprojectModal from "./modules/LcprojectModal.vue";
import ImportFileModal from '@comp/tools/ImportFileModal.vue'
import { axios as request } from "@/utils/request";

export default {
  name: "LcprojectList",
  components: { LcprojectModal, ImportFileModal },

  data() {
    return {
      loading: false,
      list: [],
      selectedRowKeys: [],

      filters: {
        name: "",
        agency: "",
        status: undefined,
        location: ""
      },

      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },

      columns: [
        {
          title: "#",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function(t, r, index) {
            return parseInt(index) + 1;
          }
        },
        { title: "Project Name", dataIndex: "name", scopedSlots: { customRender: "name" } },
        { title: "Agency", dataIndex: "agency" },
        { title: "Status", dataIndex: "status", scopedSlots: { customRender: "status" } },
        { title: "Location", dataIndex: "location" },
        { 
          title: "Start Date", 
          dataIndex: "startDate",
          customRender: function(text) {
            return !text ? "" : (text.length > 10 ? text.substring(0, 10) : text);
          }
        },
        { 
          title: "End Date", 
          dataIndex: "endDate",
          customRender: function(text) {
            return !text ? "" : (text.length > 10 ? text.substring(0, 10) : text);
          }
        },
        { title: "Budget", dataIndex: "budget", scopedSlots: { customRender: "budget" } },
        { title: "Manager", dataIndex: "manager" },
        {
          title: "Actions",
          width: 150,
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

  activated() {
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
          url: "/lcproject/select",
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
        console.error("Error loading project data:", e);
        this.list = [];
        this.pagination.total = 0;
        let errorMsg = "Failed to load project data. Please try again.";
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

    handleDetail(record) {
      this.$router.push({ name: 'ProjectDetail', query: { id: record.id, from: 'projectList' } });
    },

    openModal(record) {
      this.$refs.modal.open(record);
    },

    deleteRecord(id) {
      this.$confirm({
        title: "Are you sure you want to delete this project?",
        okText: "Confirm",
        cancelText: "Cancel",
        okType: "danger",
        onOk: async () => {
          try {
             const res = await request({
                url: `/lcproject/delete/${id}`,
                method: "delete"
              });
              if(res === 1 || res.code === 200) {
                 this.$message.success("Deleted successfully");
                 this.loadData();
              } else {
                 this.$message.error("Failed to delete");
              }
          } catch(e) {
              this.$message.error("Error deleting project");
          }
        }
      });
    },

    resetFilters() {
      this.filters = {
        name: "",
        agency: "",
        status: undefined,
        location: ""
      };
      this.pagination.current = 1;
      this.loadData();
    },

    async batchDelete() {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning("Please select at least one project to delete");
        return;
      }

      this.$confirm({
        title: `Are you sure you want to delete ${this.selectedRowKeys.length} project(s)?`,
        content: "This action cannot be undone.",
        okText: "Confirm",
        okType: "danger",
        cancelText: "Cancel",
        onOk: async () => {
          try {
            const res = await request({
              url: "/lcproject/deleteBatch",
              method: "DELETE",
              params: { ids: this.selectedRowKeys.join(",") }
            });

            if (res === 1 || res.code === 200 || (typeof res === 'number' && res > 0)) {
              this.$message.success(`Successfully deleted ${this.selectedRowKeys.length} project(s)`);
              this.selectedRowKeys = [];
              this.loadData();
            } else {
              this.$message.error(res.data && res.data.message || "Failed to delete projects");
            }
          } catch (error) {
            console.error("Batch delete failed:", error);
            this.$message.error("Failed to delete projects");
          }
        }
      });
    },

    handleImport() {
      const importExcelUrl = `/lcproject/importProject`
      const templateUrl = '/doc/project_template.xls'
      const templateName = 'Project Excel Template[Download]'
      this.$refs.modalImportForm.initModal(importExcelUrl, templateUrl, templateName)
      this.$refs.modalImportForm.title = 'Project Import'
    },
    modalFormOk() {
      this.loadData();
    },

    async handleExport() {
      try {
        this.loading = true;
        const params = {
          ...this.filters,
          currentPage: 1,
          pageSize: 10000 // Fetch all (or reasonable max) for export
        };
        
        const res = await request({
          url: "/lcproject/select",
          method: "get",
          params
        });

        if (!res || !res.data) {
          this.$message.error("Failed to fetch data for export");
          this.loading = false;
          return;
        }

        const exportList = res.data;
        const headers = ["Project Name", "Client", "Status", "Location", "Start Date", "End Date", "Budget", "Manager", "Contact Person", "Contact Phone"];
        const csvContent = [
          headers.join(","),
          ...exportList.map(item => [
            item.name || "",
            item.client || "",
            item.status || "",
            item.location || "",
            item.startDate ? (item.startDate.length > 10 ? item.startDate.substring(0, 10) : item.startDate) : "",
            item.endDate ? (item.endDate.length > 10 ? item.endDate.substring(0, 10) : item.endDate) : "",
            item.budget || "",
            item.manager || "",
            item.contactPerson || "",
            item.contactPhone || ""
          ].map(field => `"${String(field).replace(/"/g, '""')}"`).join(",")) // Escape quotes
        ].join("\n");

        const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
        const link = document.createElement("a");
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", `projects_${new Date().getTime()}.csv`);
        link.style.visibility = "hidden";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        this.$message.success("Projects exported successfully");
      } catch (error) {
        console.error("Export failed:", error);
        this.$message.error("Failed to export projects");
      } finally {
        this.loading = false;
      }
    },

    getStatusColor(status) {
      switch (status) {
        case 'new-lead': return '#56CCF2';
        case 'budget': return '#F2994A';
        case 'specify': return '#BB6BD9';
        case 'quote': return '#6FCF97';
        case 'follow-up': return '#F2C94C';
        case 'po': return '#EB5757';
        case 'active': return 'green'; // Keep for legacy
        case 'completed': return 'blue'; // Keep for legacy
        case 'on-hold': return 'orange'; // Keep for legacy
        case 'cancelled': return 'red'; // Keep for legacy
        default: return 'default';
      }
    }
  }
};
</script>

<style scoped lang="less">
.lcproject-page {
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

.status-filter-wrapper {
  position: relative;
}

.prefix-icon {
  position: absolute;
  left: 11px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 1;
  color: rgba(0, 0, 0, 0.25);
  pointer-events: none;
}

.select-with-prefix /deep/ .ant-select-selection__rendered {
  margin-left: 30px;
}
</style>