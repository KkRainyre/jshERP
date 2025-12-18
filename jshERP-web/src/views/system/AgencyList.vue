<template>
  <div class="agency-page">
    <!-- FILTERS -->
    <a-card class="filter-card" :bordered="false">
      <div class="filter-container">
        <div class="filter-item">
          <a-input v-model="queryParam.name" placeholder="Search by Agency Name" allowClear>
            <a-icon slot="prefix" type="bank" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="queryParam.tier" placeholder="Tier" allowClear>
            <a-icon slot="prefix" type="star" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="queryParam.phone" placeholder="Phone" allowClear>
            <a-icon slot="prefix" type="phone" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="queryParam.city" placeholder="City" allowClear>
            <a-icon slot="prefix" type="environment" />
          </a-input>
        </div>
        <div class="filter-actions">
          <a-button type="primary" icon="search" @click="searchQuery">Search</a-button>
          <a-button icon="redo" @click="searchReset">Reset</a-button>
        </div>
      </div>
    </a-card>

    <!-- TABLE -->
    <a-card class="table-card" :bordered="false">
      <div class="actions">
        <a-button
          v-if="btnEnableList.indexOf(1) > -1"
          @click="handleAdd"
          type="primary"
          icon="plus"
        >
          Add Agency
        </a-button>
        <a-button
          v-if="btnEnableList.indexOf(1) > -1"
          @click="batchDel"
          type="danger"
          icon="delete"
          :disabled="selectedRowKeys.length === 0"
        >
          Delete {{ selectedRowKeys.length > 0 ? `(${selectedRowKeys.length})` : '' }}
        </a-button>
        <a-button
          @click="handleImportXls()"
          icon="import"
        >
          Import
        </a-button>
        <a-button
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
                okText="Confirm"
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
  </div>
</template>

<script>
import AgencyModal from './modules/AgencyModal.vue'
import ImportFileModal from '@comp/tools/ImportFileModal.vue'
import { postAction, deleteAction } from '@api/manage'
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
        importExcelUrl: '/agency/importAgency',
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
      const templateUrl = '/doc/agency_template.xls'
      const templateName = 'Agency Excel Template[Download]'
      this.$refs.modalImportForm.initModal(importExcelUrl, templateUrl, templateName)
      this.$refs.modalImportForm.title = 'Agency Import'
    },

    /** Edit record */
    handleEdit (record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = 'Edit'
      this.$refs.modalForm.disableSubmit = false
      if (this.btnEnableList.indexOf(1) === -1) {
        this.$refs.modalForm.isReadOnly = true
      }
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
    },

    batchDel: function () {
      if (!this.url.deleteBatch) {
        this.$message.error('Please set url.deleteBatch attribute!')
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('Please select records to delete!')
        return
      }
      var ids = ''
      for (var a = 0; a < this.selectedRowKeys.length; a++) {
        ids += this.selectedRowKeys[a] + ','
      }
      var that = this
      this.$confirm({
        title: 'Confirm Delete',
        content: 'Are you sure to delete selected records?',
        okText: 'Confirm',
        okType: 'danger',
        onOk: function () {
          that.loading = true
          deleteAction(that.url.deleteBatch, { ids: ids }).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.loadData()
              that.onClearSelected()
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';

/* Modern filter and table styles */
.agency-page {
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
  gap: 8px;
}

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
.logo-thumb:hover {
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
