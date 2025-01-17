package io.github.melin.superior.common.relational.alter

import com.google.common.collect.Maps
import io.github.melin.superior.common.AlterType
import io.github.melin.superior.common.relational.FunctionId
import io.github.melin.superior.common.relational.SortType
import io.github.melin.superior.common.relational.TableId
import io.github.melin.superior.common.relational.table.ColumnRel
import java.util.HashMap

data class AlterTableAction(
    override var alterType: AlterType,
    var newTableId: TableId? = null, // 修改表，新列名称
): AlterAction() {
    var location: String? = null
    var properties: Map<String, String>? = null
}

data class DefaultAction(
    override var alterType: AlterType,
): AlterAction()

data class AlterPropsAction(
    var location: String? = null,
    var properties: HashMap<String, String> = Maps.newHashMap()
): AlterAction() {
    override var alterType: AlterType = AlterType.SET_TABLE_PROPS
    constructor(properties: HashMap<String, String>): this(null, properties)
}

data class RenameTableAction(
    var newTableId: TableId
): AlterAction() {
    override var alterType: AlterType = AlterType.RENAME_TABLE
}

data class AlterTouchPartitionAction(
    var newTableId: TableId, // 修改表，新列名称
    var partitionVals: LinkedHashMap<String, String>?
): AlterAction() {
    override var alterType: AlterType = AlterType.TOUCH_TABLE
}

data class AlterViewAction(
    var querySql: String, // 修改表，新列名称
    var inputTables: List<TableId>,
    var functionNames: HashSet<FunctionId>? = null
): AlterAction() {
    override var alterType: AlterType = AlterType.ALTER_VIEW
}

data class RefreshMvAction(
    var async: Boolean = false
) : AlterAction() {
    override var alterType: AlterType = AlterType.REFRESH_MV
}

data class AlterColumnAction(
    override var alterType: AlterType,
    var columName: String? = null, // 修改列名
    var dataType: String? = null,
    var comment: String? = null,
    var position: String? = null,
    var afterCol: String? = null,
    var setOrDrop: String? = null,
    var nullable: Boolean = true,
    var defaultExpression: String? = null,
    var dropDefault: Boolean = false,
    var ifNotExists: Boolean = false
): AlterAction() {
    var newColumName: String? = null // 修改列名，新列名称

    fun getColumn(): ColumnRel? {
        return if (columName != null) {
            ColumnRel(columName!!, dataType, comment, nullable, defaultExpression)
        } else {
            null
        }
    }
}

data class DropColumnAction(
    var columNames: ArrayList<String> = arrayListOf()
): AlterAction() {
    override var alterType: AlterType = AlterType.DROP_COLUMN

    fun firstColumn(): String {
        return columNames.first()
    }

    constructor(columName: String): this(arrayListOf(columName))
}

data class AddPartitionAction(
    var ifNotExists: Boolean = false,
    var partitions: List<LinkedHashMap<String, String>>
): AlterAction() {
    override var alterType: AlterType = AlterType.ADD_PARTITION
}

data class DropPartitionAction(
    var ifExists: Boolean = false,
    var partitions: List<LinkedHashMap<String, String>>
): AlterAction() {
    override var alterType: AlterType = AlterType.DROP_PARTITION
}

data class RenamePartitionAction(
    var fromPartitionVals: LinkedHashMap<String, String>,
    var toPartitionVals: LinkedHashMap<String, String>
): AlterAction() {
    override var alterType: AlterType = AlterType.RENAME_PARTITION
}

data class CreateIndex(
    val indexName: String,
    val indexColumnNames: ArrayList<IndexColumnName> = arrayListOf()
): AlterAction() {
    override var alterType: AlterType = AlterType.ADD_INDEX
    var intimeAction: String = "ONLINE" //mysql ONLINE & OFFLINE
    var indexCategory: String? = null
    var indexType: String? = null
    var comment: String? = null
}

data class IndexColumnName(
    val columnName: String,
    val sortType: SortType = SortType.UNKOWN,
)

data class DropIndex(
    val indexName: String,
    var ifExists: Boolean = false
): AlterAction() {
    override var alterType: AlterType = AlterType.DROP_INDEX
}