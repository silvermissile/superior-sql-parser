package io.github.melin.superior.common.relational.create

import io.github.melin.superior.common.PrivilegeType
import io.github.melin.superior.common.SqlType
import io.github.melin.superior.common.StatementType
import io.github.melin.superior.common.relational.FunctionId
import io.github.melin.superior.common.relational.abs.AbsTableStatement
import io.github.melin.superior.common.relational.TableId
import io.github.melin.superior.common.relational.table.ColumnRel

data class CreateTableAsSelect(
    override val tableId: TableId,
    val comment: String? = null,
    var lifeCycle: Int? = null,
    var partitionColumnRels: List<ColumnRel>? = null,
    var columnRels: List<ColumnRel>? = null,
    var properties: Map<String, String>? = null,
    var fileFormat: String? = null,
    var ifNotExists: Boolean = false, //是否存在 if not exists 关键字
    var locationPath: String? = null,
    var querySql: String? = null,
    val partitionColumnNames: ArrayList<String> = arrayListOf()
) : AbsTableStatement() { //是否存在 if exists 关键字
    override val statementType = StatementType.CREATE_TABLE_AS_SELECT
    override val privilegeType = PrivilegeType.CREATE
    override val sqlType = SqlType.DDL

    var replace = false
    // 建表方式：hive & spark. https://spark.apache.org/docs/3.2.0/sql-ref-syntax-ddl-create-table.html
    var createTableType: String = "hive"

    val inputTables: ArrayList<TableId> = arrayListOf()
    val functionNames: HashSet<FunctionId> = hashSetOf()

    constructor(tableId: TableId):
            this(tableId, null, null, null, null, null, null, false)

    constructor(
        tableId: TableId,
        comment: String? = null,
        ifNotExists: Boolean,
        properties: Map<String, String>? = null):
            this(tableId, comment, null, null, null, properties, null, ifNotExists)
}