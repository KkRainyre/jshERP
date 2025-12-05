<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="Agency Name" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="Please enter agency name" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="Tier" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="Please enter contact person" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="Phone" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="Please enter contact number" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery">Search</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">Reset</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? 'Close' : 'Expand' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                  </a>
                </span>
              </a-col>
            </a-row>
            <template v-if="toggleSearchStatus">
              <a-row :gutter="24">
                <a-col :md="6" :sm="24">
                  <a-form-item label="City" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="Please enter city" ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="State" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="Please enter state" ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="Postal Code" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="Please enter postal code" ></a-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator"  style="margin-top: 5px">
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="handleAdd" type="primary" icon="plus">Add</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchDel" icon="delete">Delete</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus(true)" icon="check-square">Enable</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus(false)" icon="close-square">Ban</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="handleImportXls()" icon="import">Import</a-button>
          <a-button v-if="btnEnableList.indexOf(3)>-1" @click="handleExportXls('会员信息')" icon="download">Export</a-button>
<!--          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetAdvanceIn()" icon="stock">修正预付款</a-button>-->
        </div>
        <!-- table区域-begin -->
        <div class="table-wrapper">

        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">edit</a>
              <a-divider type="vertical" />
              <a-popconfirm title="Are you sure to delete?" @confirm="() => handleDelete(record.id)">
                <a>delete</a>
              </a-popconfirm>
            </span>


<!--            marco hover to zoom in-->
            <!-- 状态渲染模板 -->
            <template slot="customRenderFlag" slot-scope="enabled">
              <a-tag v-if="enabled" color="green">启用</a-tag>
              <a-tag v-if="!enabled" color="orange">禁用</a-tag>
            </template>
            <template slot="websiteRender" slot-scope="text">
              <div v-if="text">
                <a :href="formatUrl(text)" target="_blank" class="website-link">
                  {{ text }}
                </a>
              </div>
              <span v-else>-</span>
            </template>
            <template slot="logoRender" slot-scope="logo">
              <div
                v-if="logo"
                class="logo-cell"
                @mousemove="movePreview($event)"
                @mouseleave="hidePreview"
              >
                <!-- thumbnail -->
                <img
                  :src="'data:image/jpeg;base64,' + logo"
                  class="logo-thumb"
                  alt="logo"
                />

                <!-- floating preview -->
                <div class="logo-preview-wrapper">
                  <img
                    :src="'data:image/jpeg;base64,' + logo"
                    class="logo-preview"
                  />
                </div>
              </div>

              <span v-else>-</span>
            </template>
            <template slot="agencyLink" slot-scope="text, record">
              <a @click="goToAgency(record.id)" class="agency-link">
                {{ text }}
              </a>
            </template>



          </a-table>
          </div>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <agency-modal ref="modalForm" @ok="modalFormOk"></agency-modal>
        <import-file-modal ref="modalImportForm" @ok="modalFormOk"></import-file-modal>
      </a-card>
    </a-col>
  </a-row>

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
            key: 'rowIndex',
            width: 40,
            align: "center",
            customRender: (t, r, index) => index + 1
          },
          { title: 'Logo', dataIndex: 'logo', width: 60, align: "center", scopedSlots: { customRender: 'logoRender' }},

          {
            title: 'Agency Name',
            dataIndex: 'name',
            width: 120,
            align: "center",
            scopedSlots: { customRender: 'agencyLink' }
          },

          { title: 'Tier', dataIndex: 'tier', width: 80, align: "center" },
          { title: 'Phone', dataIndex: 'phone', width: 100, align: "center" },
          { title: 'Email', dataIndex: 'email', width: 140, align: "center" },
          {title: 'Website', dataIndex: 'website', width: 140, align: "center", scopedSlots: { customRender: 'websiteRender' }},
          { title: 'City', dataIndex: 'city', width: 100, align: "center" },
          { title: 'State', dataIndex: 'state', width: 80, align: "center" },
          { title: 'Postal', dataIndex: 'postal', width: 80, align: "center" },
          { title: 'EIN', dataIndex: 'EIN', width: 120, align: "center" },

          {
            title: '操作',
            dataIndex: 'action',
            width: 100,
            align: "center",
            scopedSlots: { customRender: 'action' },
          }
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
      formatUrl(url) {
        if (!url) return '';
        return url.startsWith('http') ? url : `https://${url}`;
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .logo-cell {
    position: relative;
    display: inline-block;
  }

  .logo-thumb {
    width: 40px;
    height: 40px;
    object-fit: contain;
    border: 1px solid #eee;
    padding: 2px;
    border-radius: 4px;
    cursor: zoom-in;
    transition: transform 0.25s ease, box-shadow 0.25s ease;
  }

  /* subtle lift on hover */
  .logo-cell:hover .logo-thumb {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }

  /* hidden zoom preview */
  .logo-preview-wrapper {
    position: fixed;            /* <-- THIS is the magic */
    z-index: 99999;
    pointer-events: none;

    opacity: 0;
    transform: scale(0.8);
    transition: opacity 0.25s ease, transform 0.25s ease;

    background: #fff;
    padding: 8px;
    border-radius: 8px;
    border: 1px solid rgba(0,0,0,0.1);
    box-shadow: 0 15px 35px rgba(0,0,0,0.2);
  }


  /* When hovering → fade in + scale up smoothly */
  .logo-cell:hover .logo-preview-wrapper {
    opacity: 1;
    transform: translateY(-50%) scale(1);
  }

  /* The large preview image */
  .logo-preview {
    max-width: 240px;
    max-height: 240px;
    object-fit: contain;
    display: block;
    border-radius: 4px;
  }

  .table-wrapper {
    position: relative;
    overflow: visible !important;
  }
  .logo-thumb {
    width: 40px;
    height: 40px;
    object-fit: contain;
    border: 1px solid #eee;
    padding: 2px;
    border-radius: 4px;
    cursor: zoom-in;
    transition: 0.25s ease;
  }



</style>