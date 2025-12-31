<template>
  <div class="quote-page">
    <!-- FILTERS -->
    <a-card class="filter-card" :bordered="false">
      <div class="filter-container">
        <div class="filter-item">
          <a-input v-model="queryParam.quoteNo" placeholder="Search by Quote No" allowClear>
            <a-icon slot="prefix" type="file-text" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="queryParam.customerName" placeholder="Customer Name" allowClear>
            <a-icon slot="prefix" type="user" />
          </a-input>
        </div>
        <div class="filter-item">
          <a-input v-model="queryParam.status" placeholder="Status" allowClear>
            <a-icon slot="prefix" type="info-circle" />
          </a-input>
        </div>
        <div class="filter-item" style="display: flex; align-items: center; gap: 8px;">
          <a-input v-model="queryParam.minPrice" placeholder="Min Price" style="text-align: center" allowClear />
          <span style="color: #d9d9d9">~</span>
          <a-input v-model="queryParam.maxPrice" placeholder="Max Price" style="text-align: center" allowClear />
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
          @click="handleAdd"
          type="primary"
          icon="plus"
        >
          New Quote
        </a-button>
        <a-button
          @click="batchDel"
          type="danger"
          icon="delete"
          :disabled="selectedRowKeys.length === 0"
        >
          Delete {{ selectedRowKeys.length > 0 ? `(${selectedRowKeys.length})` : '' }}
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
              <a @click="handleEdit(record)">Edit</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="Are you sure to delete?"
                okText="Confirm"
                @confirm="() => handleDelete(record.id)"
              >
                <a>Delete</a>
              </a-popconfirm>
            </span>

            <!-- Status render -->
            <template slot="statusRender" slot-scope="text">
              <a-tag v-if="text === 'Approved'" color="green">{{ text }}</a-tag>
              <a-tag v-else-if="text === 'Pending'" color="orange">{{ text }}</a-tag>
              <a-tag v-else color="blue">{{ text || 'Draft' }}</a-tag>
            </template>

            <!-- Clickable reference -->
            <template slot="referenceLink" slot-scope="text, record">
              <a @click="handleEdit(record)">{{ text }}</a>
            </template>
          </a-table>
        </div>
      </a-card>
      
      <!-- Modal Form -->
      <quote-modal ref="modalForm" @ok="modalFormOk"></quote-modal>
  </div>
</template>

<script>
import QuoteModal from './modules/QuoteModal.vue'
import { postAction, deleteAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { filterObj } from '@/utils/util'

export default {
  name: 'QuoteList',
  mixins: [JeecgListMixin],
  components: {
    QuoteModal
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
        quoteNo: '',
        customerName: '',
        status: '',
        minPrice: '',
        maxPrice: ''
      },
      isorter: {
        column: 'create_time',
        order: 'desc'
      },
      ipagination: {
        current: 1,
        pageSize: 20,
        pageSizeOptions: ['10', '20', '50', '100'],
        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total}`
      },
      scroll: {
        x: 1000
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
          title: 'Reference',
          dataIndex: 'quoteNo',
          width: 150,
          align: 'center',
          sorter: true,
          scopedSlots: { customRender: 'referenceLink' }
        },
        {
          title: 'Customer',
          dataIndex: 'customerName',
          width: 200,
          align: 'center',
          sorter: true
        },
        {
          title: 'Total',
          dataIndex: 'totalAmount',
          width: 120,
          align: 'center',
          sorter: true,
          customRender: (text, record) => {
             if (text === null || text === undefined || text === '') return '-'
             const symbol = record.currency === 'CAD' ? 'C$' : 
                            record.currency === 'USD' ? '$' :
                            record.currency === 'CNY' ? '¥' : 
                            record.currency === 'EUR' ? '€' : '$'
             return `${symbol} ${text}`
          }
        },
        {
          title: 'Date',
          dataIndex: 'createdTime',
          width: 150,
          align: 'center',
          sorter: true,
          customRender: (text) => {
            if (!text) return ''
            return text.length > 10 ? text.substring(0, 10) : text
          }
        },
        {
          title: 'Status',
          dataIndex: 'status',
          width: 100,
          align: 'center',
          scopedSlots: { customRender: 'statusRender' }
        },
        {
          title: 'Action',
          dataIndex: 'action',
          width: 120,
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/lcquote/list',
        delete: '/lcquote/delete',
        deleteBatch: '/lcquote/deleteBatch',
        importExcelUrl: '/lcquote/importExcel',
        exportXlsUrl: '/lcquote/exportExcel',
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
        quoteNo: '',
        customerName: '',
        status: '',
        minPrice: '',
        maxPrice: ''
      }
      this.loadData(1)
    },

    /** Edit record */
    handleEdit (record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = 'Edit Quote'
    },
    
    handleAdd () {
      this.$refs.modalForm.add()
      this.$refs.modalForm.title = 'New Quote'
    },

    handleImportXls () {
      this.$message.info('Import Quotes coming soon')
    },

    batchDel: function () {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('Please select records to delete!')
        return
      }
      this.$message.info('Batch delete coming soon')
    },
    
    getQueryParams() {
      // 获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      let searchObj = {}
      searchObj.search = JSON.stringify(this.queryParam);
      var param = Object.assign(sqp, searchObj, this.isorter, this.filters);
      param.field = this.getQueryField();
      param.currentPage = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';

/* Modern filter and table styles */
.quote-page {
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

/* Button Animations */
button.ant-btn {
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

button.ant-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

button.ant-btn:active {
  transform: translateY(0);
}
</style>
