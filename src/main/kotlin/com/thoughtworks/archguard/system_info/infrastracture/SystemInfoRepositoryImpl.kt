package com.thoughtworks.archguard.system_info.infrastracture

import com.thoughtworks.archguard.system_info.domain.SystemInfo
import com.thoughtworks.archguard.system_info.domain.SystemInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SystemInfoRepositoryImpl : SystemInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi
    override fun getSystemInfo(id: Long): SystemInfo? =
            jdbi.withHandle<SystemInfo, Nothing> {
                it.createQuery("select id, system_name systemName, repo repo, sql_table `sql`," +
                        " username username, password password, repo_type repoType, scanned scanned," +
                        " quality_gate_profile_id qualityGateProfileId, updated_time updatedTime, language language," +
                        "code_path codePath, " +
                        " threshold_suite_id badSmellThresholdSuiteId, branch from system_info where id = :id")
                        .bind("id", id)
                        .mapTo<SystemInfo>()
                        .firstOrNull()
            }

    override fun getSystemInfoList(): List<SystemInfo> =
            jdbi.withHandle<List<SystemInfo>, Nothing> {
                it.createQuery("select id, system_name systemName, repo repo, sql_table `sql`, username username, " +
                        "password password, scanned scanned, quality_gate_profile_id qualityGateProfileId," +
                        "repo_type repoType, updated_time updatedTime, language language, code_path codePath, " +
                        "threshold_suite_id badSmellThresholdSuiteId, branch from system_info")
                        .mapTo<SystemInfo>()
                        .list()
            }

    override fun updateSystemInfo(systemInfo: SystemInfo): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update system_info set " +
                    "system_name = :systemName, " +
                    "repo = :repo, " +
                    "sql_table = :sql, " +
                    "username = :username, " +
                    "password = :password, " +
                    "repo_type = :repoType, " +
                    "quality_gate_profile_id = :qualityGateProfileId, " +
                    "threshold_suite_id = :badSmellThresholdSuiteId, " +
                    "branch = :branch, " +
                    "language = :language, " +
                    "code_path = :codePath " +
                    "where id = :id")
                    .bindBean(systemInfo)
                    .execute()
        }
    }

    override fun addSystemInfo(systemInfo: SystemInfo): Long {
        return jdbi.withHandle<Long, Nothing> {
            it.createUpdate("insert into system_info" +
                    "(id, system_name, repo, sql_table, username, password, repo_type, scanned, quality_gate_profile_id, " +
                    " language, code_path, threshold_suite_id, branch) " +
                    "values (:id, :systemName, " +
                    ":repo, " +
                    ":sql, " +
                    ":username, " +
                    ":password, " +
                    ":repoType, " +
                    ":scanned, " +
                    ":qualityGateProfileId, " +
                    ":language, " +
                    ":codePath, " +
                    ":badSmellThresholdSuiteId, " +
                    ":branch)")
                    .bindBean(systemInfo)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun queryBysystemName(systemName: String): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createQuery("select count(*) from system_info where system_name = :systemName")
                        .bind("systemName", systemName)
                        .mapTo(Int::class.java)
                        .first()
            }

    override fun deleteSystemInfo(id: Long): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from system_info where id = :id")
                    .bind("id", id)
                    .execute()
        }
    }

    private val TABLES = listOf(
        "code_ref_class_dependencies",
        "code_ref_class_fields",
        "code_ref_class_methods",
        "code_ref_class_parent",
        "code_ref_method_callees",
        "code_ref_method_fields",

        "code_class",
        "code_class_access",
        "code_field",
        "code_method",
        "code_annotation",
        "code_annotation_value",
        "method_access",

        "metric_code_bad_smell",
        "metric_checkstyle",
        "metric_class_coupling",
        "metric_class",
        "metric_test_bad_smell",
        "metric_circular_dependency",
        "metric_cognitive_complexity",
        "metric_dataclass",
        "method_metrics",
        "metric_module",
        "metric_package",

        "system_configure",
        "code_framework_dubbo_bean",
        "code_framework_dubbo_module",
        "code_framework_dubbo_reference_config",
        "code_framework_dubbo_service_config",

        "logic_module",
        "violation",
        "scm_commit_log",
        "scm_change_entry",
        "scm_git_hot_file",
        "scm_path_change_count",

        "container_demand",
        "container_resource",
        "container_service"
    )

    override fun deleteSystemInfoRelated() {
        val sqls = mutableListOf<String>()
        val tables = TABLES

        tables.forEach { sqls.add("delete from $it where system_id not in (select id from system_info)") }

        jdbi.withHandle<IntArray, Nothing> {
            val batch = it.createBatch()
            for (sql in sqls) {
                batch.add(sql)
            }
            batch.execute()
        }
    }
}
