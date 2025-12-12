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
          <a-input v-model="filters.client" placeholder="Client" allowClear>
            <a-icon slot="prefix" type="user" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-select v-model="filters.status" placeholder="Status" allowClear style="width: 100%">
            <a-icon slot="suffixIcon" type="flag" />
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
  </div>
</template>

<!-- f r o m 7 5  2 7 1  8 9 2 0 -->
<script>
import AgencyModal from './modules/AgencyModal.vue'
import ImportFileModal from '@comp/tools/ImportFileModal.vue'
import { postAction } from '@api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDate from '@comp/jeecg/JDate.vue'
import Vue from 'vue'
export default {
  name: "AgencyList",
  mixins:[JeecgListMixin],
  components: {
    AgencyModal,
    ImportFileModal,
    JDate
  },
  data () {
    return {
      labelCol: {
        span: 5
      },
      wrapperCol: {
        span: 18,
        offset: 1
      },
      // 查询条件
      queryParam: {
        name:'',
        tier:'',
        phone:'',
        city:'',
        state:'',
        postalCode:''
      },
      urlPath: '/system/agency',
      ipagination:{
        pageSizeOptions: ['10', '20', '30', '100', '200']
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key:'rowIndex',
          width:30,
          align:"center",
          customRender:function (t,r,index) {
            return parseInt(index)+1;
          }
        },
        {title: 'Logo', dataIndex: 'logo', width: 40, align: "center", scopedSlots: { customRender: 'logoRender' }},

        { title: 'Agency Name',dataIndex: 'supplier',width:80,align:"center",
          scopedSlots: { customRender: 'agencyLink' }},
        { title: 'Contact People', dataIndex: 'contacts',width:70,align:"center"},
        { title: 'Contact Number', dataIndex: 'telephone',width:70,align:"center"},
        { title: 'Address', dataIndex: 'phoneNum',width:100,align:"center"},
        { title: 'E-mail', dataIndex: 'email',width:80,align:"center"},
        { title: 'Website',dataIndex: 'advanceIn',width:70,align:"center"},
        { title: 'Currency', dataIndex: 'sort', width: 60,align:"center"},
        { title: 'Shipping',dataIndex: 'enabled',width:60,align:"center",

          scopedSlots: { customRender: 'customRenderFlag' }
        },

        {
          title: '操作',
          dataIndex: 'action',
          width: 80,
          align:"center",
          scopedSlots: { customRender: 'action' },
        },

      ],
      url: {
        list: "/agency/list",
        delete: "/agency/delete",
        deleteBatch: "/agency/deleteBatch",
        importExcelUrl: "/agency/importMember",
        exportXlsUrl: "/agency/exportExcel",
        batchSetStatusUrl: "/agency/batchSetStatus",
        batchSetAdvanceInUrl: "/agency/batchSetAdvanceIn"
      }
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`;
    }
  },
  methods: {
    searchReset() {
      this.queryParam = {
        type:'会员',
      }
      this.loadData(1);
    },
    handleImportXls() {
      let importExcelUrl = this.url.importExcelUrl
      let templateUrl = '/doc/member_template.xls'
      let templateName = '会员Excel模板[下载]'
      this.$refs.modalImportForm.initModal(importExcelUrl, templateUrl, templateName);
      this.$refs.modalImportForm.title = "会员导入";
    },
    handleEdit: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
      if(this.btnEnableList.indexOf(1)===-1) {
        this.$refs.modalForm.isReadOnly = true
      }
    },
    batchSetAdvanceIn() {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
      } else {
        let ids = "";
        for (let a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        let that = this;
        this.$confirm({
          title: "确认操作",
          content: "是否操作选中数据?",
          onOk: function () {
            that.loading = true;
            postAction(that.url.batchSetAdvanceInUrl, {ids: ids}).then((res) => {
              if(res.code === 200){
                that.$message.info('修正预付款成功！');
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.data.message);
              }
            }).finally(() => {
              that.loading = false;
            });
          }
        });
      }
    },

    movePreview(e) {
      const preview = e.currentTarget.querySelector(".logo-preview-wrapper");
      if (!preview) return;

      preview.style.left = e.clientX + 20 + "px";   // right of cursor
      preview.style.top  = e.clientY + 20 + "px";   // below cursor
      preview.style.opacity = 1;
      preview.style.transform = "scale(1)";
    },

    hidePreview(e) {
      const preview = e.currentTarget.querySelector(".logo-preview-wrapper");
      if (!preview) return;

      preview.style.opacity = 0;
      preview.style.transform = "scale(0.8)";
    },
    goToAgency(id) {
      this.$router.push(`/agency/${id}`);
    },

    handleImport() {
      this.$message.info("Import functionality - Coming soon");
      // TODO: Implement import functionality
      // You can use a file upload modal here
    },

    handleExport() {
      try {
        // Simple CSV export
        const headers = ["Project Name", "Client", "Status", "Location", "Start Date", "End Date", "Budget", "Manager", "Contact Person", "Contact Phone"];
        const csvContent = [
          headers.join(","),
          ...this.list.map(item => [
            item.name || "",
            item.client || "",
            item.status || "",
            item.location || "",
            item.startDate || "",
            item.endDate || "",
            item.budget || "",
            item.manager || "",
            item.contactPerson || "",
            item.contactPhone || ""
          ].map(field => `"${field}"`).join(","))
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
      }
    }

  }
}
</script>

<style scoped lang="less">
.lcproject-page {
  padding: 16px;
  background: #f0f2f5;
}

.filter-card {
  margin-bottom: 16px;
  border-radius: 4px;
}

.filter-container {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-item {
  flex: 1;
  min-width: 200px;
}

.filter-actions {
  display: flex;
  gap: 8px;
}

.table-card {
  border-radius: 4px;
}

.actions {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>