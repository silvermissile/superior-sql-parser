package io.github.melin.superior.parser.flink

import io.github.melin.superior.common.*
import io.github.melin.superior.common.relational.Statement
import io.github.melin.superior.common.relational.TableId
import io.github.melin.superior.common.relational.table.ColumnRel

data class FlinkCdcCreateTable(
    var sinkTableId: TableId,
    var sourceTableId: TableId,
): Statement() {
    override val statementType = StatementType.FLINK_CDC_CTAS
    override val privilegeType = PrivilegeType.CREATE
    override val sqlType = SqlType.DML

    var sinkOptions: HashMap<String, String>? = null
    var sourceOptions: HashMap<String, String>? = null
    var computeCols: List<ColumnRel>? = null
}

data class FlinkCdcCreateDatabase(
    var sinkCatalogName: String?,
    var sinkDatabaseName: String,
    var sourceCatalogName: String?,
    var sourceDatabaseName: String,
    var includeTable: String
): Statement() {
    override val statementType = StatementType.FLINK_CDC_CDAS
    override val privilegeType = PrivilegeType.CREATE
    override val sqlType = SqlType.DML

    var sinkOptions: HashMap<String, String>? = null
    var sourceOptions: HashMap<String, String>? = null
    var excludeTable: String? = null
}