<template>
  <a-modal
    :visible="visible"
    :title="isEdit ? 'Edit Quote' : 'New Quote'"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    width="1000px"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="Quote Reference">
            <a-input
              v-decorator="[
                'quoteNo',
                {
                  rules: [
                    { required: true, message: 'Please input Quote Reference!' },
                    { max: 50, message: 'Max 50 characters' }
                  ]
                }
              ]"
              placeholder="e.g. Q2023-001"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="Customer Name">
            <a-input
              v-decorator="[
                'customerName',
                {
                  rules: [
                    { required: true, message: 'Please input Customer Name!' },
                    { max: 255, message: 'Max 255 characters' }
                  ]
                }
              ]"
              placeholder="Customer Name"
            />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="Status">
            <a-select 
              v-decorator="['status', { initialValue: 'Draft' }]"
              placeholder="Select Status"
            >
              <a-select-option value="Draft">Draft</a-select-option>
              <a-select-option value="Pending">Pending</a-select-option>
              <a-select-option value="Approved">Approved</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>

        <!-- Spec Code Generator Section -->
        <a-col :span="24">
          <a-card title="Specification Code Generator" size="small" style="margin-bottom: 20px; background: #f5f5f5;">
            <a-row :gutter="8" style="margin-bottom: 10px">
               <a-col :span="6">
                  <div class="code-label">Product Family</div>
                  <a-select v-model="selectedProduct" @change="handleProductChange" style="width: 100%">
                    <a-select-option v-for="(cfg, key) in productConfigs" :key="key" :value="key">
                      {{ cfg.label }}
                    </a-select-option>
                  </a-select>
               </a-col>
            </a-row>

            <a-row :gutter="8">
               <!-- Prefix -->
               <a-col :span="2">
                 <div class="code-label">Prefix</div>
                 <a-input :value="currentConfig.prefix" disabled />
               </a-col>

               <!-- Shape -->
               <a-col :span="3">
                 <div class="code-label">Shape</div>
                 <a-select v-model="generator.shape" placeholder="Shape">
                    <a-select-option v-for="opt in currentOptions.shape" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                    </a-select-option>
                 </a-select>
               </a-col>

               <!-- Dimension -->
               <a-col :span="3">
                 <div class="code-label">Dimension</div>
                 <a-input v-model="generator.dimension" placeholder="WxL" />
               </a-col>

               <!-- CCT -->
               <a-col :span="2">
                 <div class="code-label">CCT</div>
                 <a-select v-model="generator.cct" placeholder="CCT">
                   <a-select-option v-for="opt in currentOptions.cct" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Control -->
               <a-col :span="2">
                 <div class="code-label">Control</div>
                 <a-select v-model="generator.control" placeholder="Ctrl">
                   <a-select-option v-for="opt in currentOptions.control" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Profile -->
               <a-col :span="2">
                 <div class="code-label">Profile</div>
                 <a-select v-model="generator.profile" placeholder="Prof">
                   <a-select-option v-for="opt in currentOptions.profile" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Color -->
               <a-col :span="3">
                 <div class="code-label">Color</div>
                 <a-select v-model="generator.color" placeholder="Color">
                   <a-select-option v-for="opt in currentOptions.color" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Mounting -->
               <a-col :span="3">
                 <div class="code-label">Mounting</div>
                 <a-select v-model="generator.mounting" placeholder="Mnt">
                   <a-select-option v-for="opt in currentOptions.mounting" :key="opt.value" :value="opt.value">
                       {{ opt.label }}
                   </a-select-option>
                 </a-select>
               </a-col>

               <!-- Suffix -->
               <a-col :span="2">
                 <div class="code-label">Suffix</div>
                 <a-input :value="currentConfig.suffix" disabled />
               </a-col>
               
               <!-- Generate Button -->
               <a-col :span="2">
                 <div class="code-label" style="visibility: hidden">Action</div>
                 <a-button type="primary" icon="download" @click="addFromGenerator">Add</a-button>
               </a-col>
            </a-row>
            <a-row style="margin-top: 10px">
              <a-col :span="24">
                <strong>Preview: </strong> <span style="font-family: monospace; font-size: 16px; color: #1890ff; margin-right: 10px;">{{ generatedSpecCode }}</span>
                <a-button size="small" icon="copy" @click="copyCode">Copy</a-button>
              </a-col>
            </a-row>
          </a-card>
        </a-col>

        <a-col :span="24">
           <h3>Product Items</h3>
           <a-button type="dashed" style="width: 100%; margin-bottom: 8px" @click="addItem">
             <a-icon type="plus" /> Add Product
           </a-button>
           
           <a-table
             :columns="paymentTableColumns"
             :dataSource="items"
             :pagination="false"
             rowKey="tempId"
             size="small"
             bordered
           >
             <template slot="itemName" slot-scope="text, record">
               <a-input v-model="record.itemName" placeholder="Item Name" />
             </template>
             <template slot="quantity" slot-scope="text, record">
               <a-input-number v-model="record.quantity" :min="1" @change="onQtyChange(record)" style="width: 100%" />
             </template>
             <template slot="unitPrice" slot-scope="text, record">
               <a-input-number v-model="record.unitPrice" :min="0" @change="onPriceChange(record)" style="width: 100%" />
             </template>
             <template slot="lineTotal" slot-scope="text, record">
               <span>{{ (record.quantity * record.unitPrice).toFixed(2) }}</span>
             </template>
             <template slot="action" slot-scope="text, record, index">
               <a @click="deleteItem(index)">Delete</a>
             </template>
           </a-table>
        <br/>
        </a-col>

        <a-col :span="24">
          <a-form-item label="Total Amount (Auto-Calculated)">
             <a-input-number
               v-decorator="['totalAmount']" 
               style="width: 100%"
               :formatter="value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
               :parser="value => value.replace(/\$\s?|(,*)/g, '')"
               disabled
             />
          </a-form-item>
        </a-col>

      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { axios as request } from '@/utils/request'

export default {
  name: 'QuoteModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,
      isEdit: false,
      model: {},
      form: this.$form.createForm(this),
      items: [],
      // Generator State
      selectedProduct: 'LumosCielo',
      productConfigs: {
        'LumosCielo': {
           label: 'LumosCielo (WW)',
           prefix: 'BAWW',
           suffix: 'LU',
           options: {
             shape: [
               { value: 'SQ', label: 'SQ (Square)' },
               { value: 'RE', label: 'RE (Rectangle)' },
               { value: 'TR', label: 'TR (Triangle)' },
               { value: 'RO', label: 'RO (Round)' },
               { value: 'CU', label: 'CU (Custom)' }
             ],
             cct: [{ value: 'WW', label: 'WW' }],
             control: [{ value: 'DM', label: 'DM' }],
             profile: ['076', '105', '120', '140'].map(v => ({ value: v, label: v })),
             color: [
               { value: 'Cl', label: 'Cl (Clear)' },
               { value: 'Wh', label: 'Wh (White)' },
               { value: 'Bl', label: 'Bl (Black)' },
               { value: 'Ra', label: 'Ra (#RAL)' }
             ],
             mounting: [
               { value: 'Sf', label: 'Sf (Surface)' },
               { value: 'Su', label: 'Su (Suspended)' },
               { value: 'Re', label: 'Re (Recessed)' }
             ]
           }
        },
        'LumosTerra': {
           label: 'LumosTerra (BATR)',
           prefix: 'BATR',
           suffix: 'LU',
           options: {
             shape: [
               { value: 'LN', label: 'LN (Linear)' },
               { value: 'CR', label: 'CR (Curve)' }
             ],
             cct: [
                { value: '30K', label: '30K (3000K)' },
                { value: '40K', label: '40K (4000K)' }
             ],
             control: [
                { value: 'ND', label: 'ND (Non-Dim)' },
                { value: '0-10', label: '0-10 (0-10V)' }
             ],
             profile: ['050', '080'].map(v => ({ value: v, label: v })),
             color: [
               { value: 'Wh', label: 'Wh (White)' },
               { value: 'Bk', label: 'Bk (Black)' }
             ],
             mounting: [
               { value: 'Sf', label: 'Sf (Surface)' }
             ]
           }
        }
      },
      generator: {
        shape: undefined,
        dimension: '',
        cct: undefined,
        control: undefined,
        profile: undefined,
        color: undefined,
        mounting: undefined
      },
      paymentTableColumns: [
        { title: 'Item Name', dataIndex: 'itemName', scopedSlots: { customRender: 'itemName' } },
        { title: 'Qty', dataIndex: 'quantity', width: 80, scopedSlots: { customRender: 'quantity' } },
        { title: 'Unit Price', dataIndex: 'unitPrice', width: 120, scopedSlots: { customRender: 'unitPrice' } },
        { title: 'Total', dataIndex: 'lineTotal', width: 120, scopedSlots: { customRender: 'lineTotal' } },
        { title: 'Action', dataIndex: 'action', width: 60, align: 'center', scopedSlots: { customRender: 'action' } }
      ]
    }
  },
  computed: {
    currentConfig() {
      return this.productConfigs[this.selectedProduct] || {}
    },
    currentOptions() {
      return this.currentConfig.options || {}
    },
    generatedSpecCode() {
      const g = this.generator
      const c = this.currentConfig
      if (!c.prefix) return ''
      
      const dim = g.dimension || '___'
      // Only include fields if they are selected/present?
      // For now assume strictly positional.
      return `${c.prefix}-${g.shape || '_'}-${dim}-${g.cct || '_'}-${g.control || '_'}-${g.profile || '_'}-${g.color || '_'}-${g.mounting || '_'}-${c.suffix}`
    }
  },
  methods: {
    add () {
      this.edit({})
      this.items = []
      this.resetGenerator()
    },
    
    // Reset generator defaults whenever product changes or modal opens
    resetGenerator() {
      const defaults = {}
      const opts = this.currentOptions
      
      // Auto-select first option if available
      defaults.shape = opts.shape && opts.shape.length ? opts.shape[0].value : undefined
      defaults.cct = opts.cct && opts.cct.length ? opts.cct[0].value : undefined
      defaults.control = opts.control && opts.control.length ? opts.control[0].value : undefined
      defaults.profile = opts.profile && opts.profile.length ? opts.profile[0].value : undefined
      defaults.color = opts.color && opts.color.length ? opts.color[0].value : undefined
      defaults.mounting = opts.mounting && opts.mounting.length ? opts.mounting[0].value : undefined
      
      defaults.dimension = ''
      
      this.generator = defaults
    },

    handleProductChange() {
       this.resetGenerator()
    },
    
    addFromGenerator() {
      if (!this.generator.dimension) {
        this.$message.warning('Please enter a dimension')
        return
      }
      this.items.push({
        tempId: Date.now(),
        itemName: this.generatedSpecCode,
        quantity: 1,
        unitPrice: 0
      })
      // Clear dimension after adding to allow next quick entry? Or keep it?
      // Keeping it fits better if they just want to change one attr.
    },
    
    copyCode() {
      const text = this.generatedSpecCode
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => {
          this.$message.success('Code copied to clipboard')
        }).catch(() => {
          this.$message.error('Failed to copy')
        })
      } else {
        // Fallback
        const textArea = document.createElement("textarea")
        textArea.value = text
        document.body.appendChild(textArea)
        textArea.select()
        try {
          document.execCommand('copy')
          this.$message.success('Code copied to clipboard')
        } catch (err) {
          this.$message.error('Failed to copy')
        }
        document.body.removeChild(textArea)
      }
    },

    edit (record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.isEdit = !!record.id
      this.items = this.model.items || [] // Should ideally fetch items if edit
      
      this.$nextTick(() => {
        this.form.setFieldsValue({
          quoteNo: this.model.quoteNo,
          customerName: this.model.customerName,
          status: this.model.status || 'Draft',
          currency: this.model.currency || 'CAD',
          totalAmount: this.model.totalAmount
        })
      })
    },
    addItem() {
      this.items.push({
        tempId: Date.now(),
        itemName: '',
        quantity: 1,
        unitPrice: 0
      })
    },
    deleteItem(index) {
      this.items.splice(index, 1)
      this.calculateTotal()
    },
    onQtyChange(record) {
      this.calculateTotal()
    },
    onPriceChange(record) {
       this.calculateTotal()
    },
    calculateTotal() {
      let total = 0
      this.items.forEach(item => {
        total += (item.quantity * item.unitPrice)
      })
      this.form.setFieldsValue({ totalAmount: total })
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    handleOk () {
      const that = this
      // Validate inputs
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          
          if (!this.model.id) {
            httpurl = '/lcquote/add'
            method = 'post'
          } else {
            httpurl = '/lcquote/update'
            method = 'post'
          }

          const formData = Object.assign(this.model, values)
          formData.items = this.items // Append items
          
          request({
            url: httpurl,
            method: method,
            data: formData
          }).then((res) => {
            if (res === 200 || res === 1 || res.code === 200) { // Check various success responses
              that.$message.success('Saved successfully')
              that.$emit('ok')
              that.close()
            } else {
               that.$message.warning(res.message || 'Operation failed')
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }
      })
    },
    handleCancel () {
      this.close()
    }
  }
}
</script>
