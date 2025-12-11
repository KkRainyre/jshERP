<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- Search Area -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="Agency Name" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    v-model="queryParam.name"
                    placeholder="Please enter agency name"
                    allowClear
                  />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="Tier" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    v-model="queryParam.tier"
                    placeholder="Please enter tier"
                    allowClear
                  />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="Phone" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input
                    v-model="queryParam.phone"
                    placeholder="Please enter contact number"
                    allowClear
                  />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <span
                  style="float: left; overflow: hidden;"
                  class="table-page-search-submitButtons"
                >
                  <a-button type="primary" @click="searchQuery">Search</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">Reset</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? 'Close' : 'Expand' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                  </a>
                </span>
              </a-col>
            </a-row>

            <template v-if="toggleSearchStatus">
              <a-row :gutter="24">
                <a-col :md="6" :sm="24">
                  <a-form-item label="City" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                      v-model="queryParam.city"
                      placeholder="Please enter city"
                      allowClear
                    />
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="State" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                      v-model="queryParam.state"
                      placeholder="Please enter state"
                      allowClear
                    />
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="Postal Code" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                      v-model="queryParam.postalCode"
                      placeholder="Please enter postal code"
                      allowClear
                    />
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-form>
        </div>

        <!-- Operator Area -->
        <div class="table-operator" style="margin-top: 5px">
          <a-button
            v-if="btnEnableList.indexOf(1) > -1"
            @click="handleAdd"
            type="primary"
            icon="plus"
          >
            Add
          </a-button>
          <a-button
            v-if="btnEnableList.indexOf(1) > -1"
            @click="batchDel"
            icon="delete"
          >
            Delete
          </a-button>
          <a-button
            v-if="btnEnableList.indexOf(1) > -1"
            @click="batchSetStatus(true)"
            icon="check-square"
          >
            Enable
          </a-button>
          <a-button
            v-if="btnEnableList.indexOf(1) > -1"
            @click="batchSetStatus(false)"
            icon="close-square"
          >
            Ban
          </a-button>
          <a-button
            v-if="btnEnableList.indexOf(1) > -1"
            @click="handleImportXls()"
            icon="import"
          >
            Import
          </a-button>
          <a-button
            v-if="btnEnableList.indexOf(3) > -1"
            @click="handleExportXls('Agency List')"
            icon="download"
          >
            Export
          </a-button>
        </div>

        <!-- Table Area -->
        <div class="table-wrapper">
          <a-table
            ref="table"
            size="middle"
            bordered
            :rowKey="rowKey"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="rowSelection"
            @change="handleTableChange"
          >
            <!-- Action column -->
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">edit</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="Are you sure to delete?"
                @confirm="() => handleDelete(record.id)"
              >
                <a>delete</a>
              </a-popconfirm>
            </span>

            <!-- Status tags (if you ever use enabled flag) -->
            <template slot="customRenderFlag" slot-scope="enabled">
              <a-tag v-if="enabled" color="green">启用</a-tag>
              <a-tag v-else color="orange">禁用</a-tag>
            </template>

            <!-- Website render -->
            <template slot="websiteRender" slot-scope="text">
              <div v-if="text">
                <a :href="formatUrl(text)" target="_blank" class="website-link">
                  {{ text }}
                </a>
              </div>
              <span v-else>-</span>
            </template>

            <!-- Logo render (CSS-only zoom, no JS mousemove bullshit) -->
            <template slot="logoRender" slot-scope="record">
              <div v-if="record.logoThumb" class="logo-cell">
                <img
                  :src="'data:image/jpeg;base64,' + record.logoThumb"
                  class="logo-thumb"
                  loading="lazy"
                />

                <div class="logo-preview-wrapper">
                  <img
                    :src="'data:image/jpeg;base64,' + record.logo"
                    class="logo-preview"
                  />
                </div>
              </div>
              <span v-else>-</span>
            </template>


            <!-- Clickable agency name -->
            <template slot="agencyLink" slot-scope="text, record">
              <router-link :to="{ name: 'AgencyDetail', params: { id: record.id }}">
                {{ text }}
              </router-link>
            </template>
          </a-table>
        </div>

        <!-- Modals -->
        <agency-modal ref="modalForm" @ok="modalFormOk"></agency-modal>
        <import-file-modal ref="modalImportForm" @ok="modalFormOk"></import-file-modal>
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
import AgencyModal from './modules/AgencyModal.vue'
import ImportFileModal from '@comp/tools/ImportFileModal.vue'
import { postAction } from '@api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JDate from '@comp/jeecg/JDate.vue'

export default {
  name: 'AgencyList',
  mixins: [JeecgListMixin],
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
      // Query conditions
      queryParam: {
        name: '',
        tier: '',
        phone: '',
        city: '',
        state: '',
        postalCode: ''
      },
      urlPath: '/system/agency',
      ipagination: {
        // JeecgListMixin will merge into this
        current: 1,
        pageSize: 20,
        pageSizeOptions: ['10', '20', '50', '100'],
        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total}`
      },
      scroll: {
        x: 1200
      },
      // Table columns
      columns: [
        {
          title: '#',
          key: 'rowIndex',
          width: 50,
          align: 'center',
          customRender: (t, r, index) => index + 1
        },
        {
          title: 'Logo',
          dataIndex: 'logo',
          width: 70,
          align: 'center',
          scopedSlots: { customRender: 'logoRender' }
        },
        {
          title: 'Agency Name',
          dataIndex: 'name',
          width: 160,
          align: 'center',
          scopedSlots: { customRender: 'agencyLink' }
        },
        { title: 'Tier', dataIndex: 'tier', width: 80, align: 'center' },
        { title: 'Phone', dataIndex: 'phone', width: 120, align: 'center' },
        { title: 'Email', dataIndex: 'email', width: 180, align: 'center' },
        {
          title: 'Website',
          dataIndex: 'website',
          width: 180,
          align: 'center',
          scopedSlots: { customRender: 'websiteRender' }
        },
        { title: 'City', dataIndex: 'city', width: 120, align: 'center' },
        { title: 'State', dataIndex: 'state', width: 80, align: 'center' },
        { title: 'Postal', dataIndex: 'postal', width: 100, align: 'center' },
        { title: 'EIN', dataIndex: 'ein', width: 120, align: 'center' },
        {
          title: 'Action',
          dataIndex: 'action',
          width: 120,
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/agency/list',
        delete: '/agency/delete',
        deleteBatch: '/agency/deleteBatch',
        importExcelUrl: '/agency/importMember',
        exportXlsUrl: '/agency/exportExcel',
        batchSetStatusUrl: '/agency/batchSetStatus',
        batchSetAdvanceInUrl: '/agency/batchSetAdvanceIn'
      }
    }
  },
  computed: {
    importExcelUrl () {
      return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`
    },
    rowSelection () {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    },
    rowKey () {
      return 'id'
    }
  },
  methods: {
    /** Reset search */
    searchReset () {
      this.queryParam = {
        name: '',
        tier: '',
        phone: '',
        city: '',
        state: '',
        postalCode: ''
      }
      this.loadData(1)
    },

    /** Import */
    handleImportXls () {
      const importExcelUrl = this.url.importExcelUrl
      const templateUrl = '/doc/member_template.xls'
      const templateName = '会员Excel模板[下载]'
      this.$refs.modalImportForm.initModal(importExcelUrl, templateUrl, templateName)
      this.$refs.modalImportForm.title = '会员导入'
    },

    /** Edit record */
    handleEdit (record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = false
      if (this.btnEnableList.indexOf(1) === -1) {
        this.$refs.modalForm.isReadOnly = true
      }
    },

    /** Batch adjust prepayment (if you even use it) */
    batchSetAdvanceIn () {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return
      }
      const ids = this.selectedRowKeys.join(',')
      const that = this
      this.$confirm({
        title: '确认操作',
        content: '是否操作选中数据?',
        onOk () {
          that.loading = true
          postAction(that.url.batchSetAdvanceInUrl, { ids }).then(res => {
            if (res.code === 200) {
              that.$message.info('修正预付款成功！')
              that.loadData()
              that.onClearSelected()
            } else {
              that.$message.warning(res.data.message)
            }
          }).finally(() => {
            that.loading = false
          })
        }
      })
    },

    /** Navigate to detail page – ONLY pass ID, not huge base64 blob */
    goToAgency (record) {
      this.$router.push({
        name: 'AgencyDetail',
        params: { id: record.id }
      })
    },

    formatUrl (url) {
      if (!url) return ''
      return url.startsWith('http') ? url : `https://${url}`
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';

.table-wrapper {
  position: relative;
  overflow: visible !important;
}

/* Logo cell + preview */
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
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

/* subtle lift on hover */
.logo-cell:hover .logo-thumb {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* Hidden zoom preview – CSS only */
.logo-preview-wrapper {
  position: absolute;
  top: 50%;
  left: 110%;
  transform: translateY(-50%) scale(0.8);
  transform-origin: left center;
  z-index: 20;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s ease, transform 0.2s ease;

  background: #fff;
  padding: 8px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.logo-cell:hover .logo-preview-wrapper {
  opacity: 1;
  transform: translateY(-50%) scale(1);
}

/* Large preview image */
.logo-preview {
  max-width: 240px;
  max-height: 240px;
  object-fit: contain;
  display: block;
  border-radius: 4px;
}
</style>
