<template>
  <a-card :bordered="false">
    <!-- Search Area -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="Reference">
              <a-input placeholder="Quote Reference" v-model="queryParam.reference"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
             <span class="table-page-search-submitButtons">
               <a-button type="primary" @click="$refs.table.refresh(true)">Query</a-button>
               <a-button style="margin-left: 8px" @click="() => queryParam = {}">Reset</a-button>
             </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- Operator -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="$message.info('Create Quote feature coming soon')">New Quote</a-button>
    </div>

    <!-- Table -->
    <a-table
      ref="table"
      size="default"
      :columns="columns"
      :dataSource="data"
      :pagination="pagination"
      :loading="loading"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">Edit</a>
        <a-divider type="vertical" />
        <a @click="handleDelete(record)">Delete</a>
      </span>
    </a-table>
  </a-card>
</template>

<script>
export default {
  name: 'QuoteList',
  data () {
    return {
      queryParam: {},
      columns: [
        {
          title: 'Reference',
          dataIndex: 'reference'
        },
        {
          title: 'Customer',
          dataIndex: 'customer'
        },
        {
          title: 'Status',
          dataIndex: 'status'
        },
        {
          title: 'Total',
          dataIndex: 'total'
        },
        {
          title: 'Action',
          dataIndex: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      data: [],
      loading: false,
      pagination: {
        total: 0,
        pageSize: 10,
        showSizeChanger: true,
        pageSizeOptions: ['10', '20', '30', '40'],
        showTotal: total => `Total ${total} items`
      }
    }
  },
  methods: {
    handleEdit (record) {
      this.$message.info('Edit coming soon')
    },
    handleDelete (record) {
      this.$message.info('Delete coming soon')
    }
  }
}
</script>
