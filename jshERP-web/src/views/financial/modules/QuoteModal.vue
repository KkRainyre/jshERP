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
              disabled
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
        <!-- Hidden currency field to ensure it is submitted -->
        <a-form-item v-show="false">
          <a-input v-decorator="['currency', { initialValue: 'CAD' }]" />
        </a-form-item>

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
                <a-popconfirm title="Sure to delete?" @confirm="deleteItem(index)">
                  <a-icon type="delete" style="color: red; cursor: pointer"/>
                </a-popconfirm>
             </template>
           </a-table>
        <br/>
        </a-col>

        <a-col :span="24">
           <a-row :gutter="16">
             <a-col :span="6">
                <a-form-item label="Country">
                   <a-select v-model="targetCountry" @change="handleCountryChange">
                      <a-select-option value="CA">Canada</a-select-option>
                      <a-select-option value="US">USA</a-select-option>
                   </a-select>
                </a-form-item>
             </a-col>
             <a-col :span="6">
                <a-form-item label="State/Province">
                   <a-select v-model="targetProvince" @change="handleProvinceChange" show-search optionFilterProp="children">
                      <a-select-option v-for="(rate, key) in currentProvinces" :key="key" :value="key">
                         {{ key }}
                      </a-select-option>
                   </a-select>
                </a-form-item>
             </a-col>
             <a-col :span="4">
                <a-form-item label="Subtotal">
                   <span style="font-size: 16px; line-height: 40px;">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ subTotal.toFixed(2) }}</span>
                </a-form-item>
             </a-col>
             <a-col :span="4">
                <a-form-item :label="`Tax (${(taxRate * 100).toFixed(2)}%)`">
                   <span style="font-size: 16px; line-height: 40px;">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ taxAmount.toFixed(2) }}</span>
                </a-form-item>
             </a-col>
             <a-col :span="4">
                 <a-form-item label="Total Amount">
                   <span style="font-size: 18px; font-weight: bold; line-height: 40px; color: #52c41a">{{ targetCountry === 'CA' ? 'C$' : '$' }}{{ form.getFieldValue('totalAmount') }}</span>
                   <!-- Hidden input to store value -->
                   <a-input-number v-show="false" v-decorator="['totalAmount']"/>
                 </a-form-item>
             </a-col>
             <!-- Hidden Ext3 for Operator -->
             <a-form-item v-show="false">
               <a-input v-decorator="['ext3']" />
             </a-form-item>
           </a-row>
        </a-col>

      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { axios as request } from '@/utils/request'
import { getAction } from '@/api/manage'
import { mapGetters } from 'vuex'
import pick from 'lodash.pick'
import moment from "moment"

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
      ],
      // Tax Logic
      targetCountry: 'CA',
      targetProvince: 'Ontario',
      subTotal: 0,
      taxAmount: 0,
      taxRate: 0.13,
      taxRates: {
        'CA': {
           'Alberta': 0.05,
           'British Columbia': 0.12,
           'Manitoba': 0.12,
           'New Brunswick': 0.15,
           'Newfoundland and Labrador': 0.15,
           'Northwest Territories': 0.05,
           'Nova Scotia': 0.15,
           'Nunavut': 0.05,
           'Ontario': 0.13,
           'Prince Edward Island': 0.15,
           'Quebec': 0.14975,
           'Saskatchewan': 0.11,
           'Yukon': 0.05
        },
        'US': {
           'California': 0.0725,
           'New York': 0.088,
           'Texas': 0.0625,
           'Florida': 0.06,
           'Illinois': 0.0625,
           'Pennsylvania': 0.06,
           'Ohio': 0.0575,
           'Georgia': 0.04,
           'North Carolina': 0.0475
           // Add more as needed
        }
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo']),
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
    },
    currentProvinces() {
       return this.taxRates[this.targetCountry] || {}
    }
  },
  methods: {
    add () {
      // Auto-generate Quote No: Q + YYYYMMDD + - + 3 Random Digits
      const date = new Date().toISOString().slice(0, 10).replace(/-/g, '')
      const random = Math.floor(Math.random() * 900) + 100
      const genQuoteNo = `Q${date}-${random}`

      this.edit({ quoteNo: genQuoteNo })
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
      // Allow adding even with empty dimension (will use placeholder)
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
        
        // Restore Location from ext fields if available
        if (this.model.ext1) {
            this.targetCountry = this.model.ext1
        }
        if (this.model.ext2) {
            this.targetProvince = this.model.ext2
            // Trigger tax update
            this.handleProvinceChange()
        } else {
             // Default if new or legacy
            this.targetCountry = 'CA'
            this.targetProvince = 'Ontario'
            this.handleProvinceChange()
        }
        
        // Load Operator (ext3) if exists, else it will be set on save
        if(this.model.ext3) {
           this.form.setFieldsValue({ ext3: this.model.ext3 })
        }

        // Fetch detailed items if editing
        if (this.isEdit) {
           this.loadDetail(this.model.id)
        }
      })
    },
    loadDetail(id) {
       // Since the controller returns the object directly or wrapped, we try getAction
       // Note: Helper implementation might vary, assuming standard getAction usage
       getAction('/lcquote/get/' + id).then(res => {
          let fetchedItems = []
          
          if (res && res.items) {
             fetchedItems = res.items
          } else if (res && res.data && res.data.items) {
             fetchedItems = res.data.items
          } else if (res && res.result && res.result.items) {
             fetchedItems = res.result.items
          }

          if (fetchedItems) {
             // Inject tempId for frontend table stability
             this.items = fetchedItems.map(item => ({
               ...item,
               // Use existing ID as tempId or generate one if missing (shouldn't happen for DB items)
               tempId: item.id || Date.now() + Math.random()
             }))
             this.calculateTotal()
          }
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
    handleCountryChange() {
       // Reset province when country changes
       this.targetProvince = undefined 
       this.taxRate = 0
       
       // Auto-set Currency
       const currency = this.targetCountry === 'CA' ? 'CAD' : 'USD'
       console.log('Setting currency to:', currency)
       this.form.setFieldsValue({ currency: currency })

       this.calculateTotal()
    },
    handleProvinceChange() {
       const rates = this.taxRates[this.targetCountry]
       if (rates && this.targetProvince) {
          this.taxRate = rates[this.targetProvince] || 0
       } else {
          this.taxRate = 0
       }
       this.calculateTotal()
    },
    calculateTotal() {
      let total = 0
      this.items.forEach(item => {
        total += (item.quantity * item.unitPrice)
      })
      this.subTotal = total
      this.taxAmount = total * this.taxRate
      const grandTotal = total + this.taxAmount
      
      this.form.setFieldsValue({ totalAmount: grandTotal.toFixed(2) })
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    handleOk () {
      const that = this
      // Validate inputs
      this.form.validateFields((err, values) => {
        console.log('Form Values on Save:', values) 
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

          const formData = Object.assign({}, this.model, values)
          
          // Force include totalAmount in case it was excluded due to 'disabled'
          formData.totalAmount = this.form.getFieldValue('totalAmount')
          
          // Add Subtotal and Tax Amount
          formData.subtotal = this.subTotal
          formData.taxAmount = this.taxAmount
          
          // Persist Location data using extra fields (ext1, ext2)
          formData.ext1 = this.targetCountry
          formData.ext2 = this.targetProvince
          
          // Persist Operator (ext3)
          // If editing, use existing ext3. If new, use current user.
          let currentOperator = this.form.getFieldValue('ext3')
          if (!currentOperator && this.userInfo) {
             // userInfo getter might be function or object depending on implementation in this project
             // Based on Logo.vue it seems to be an object: const user = this.userInfo
             // But UserMenu.vue says const user = this.userInfo()
             // access safely
             const u = typeof this.userInfo === 'function' ? this.userInfo() : this.userInfo
             if(u && u.username) {
                currentOperator = u.username
             }
          }
          formData.ext3 = currentOperator

          // Filter out tempId and non-backend fields
          formData.items = this.items.map(item => ({
            id: item.id,
            itemName: item.itemName,
            quantity: item.quantity,
            unitPrice: item.unitPrice,
            lineTotal: item.lineTotal // Included just in case, though calculated
          }))
          
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
