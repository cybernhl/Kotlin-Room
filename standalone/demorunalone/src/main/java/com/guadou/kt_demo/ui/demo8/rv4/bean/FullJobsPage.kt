package com.guadou.kt_demo.ui.demo8.rv4.bean

data class FullJobsPage(
    val count: Int,
    val countPage: Int,
    val curPage: Int,
    val list: List<FullJobs>,
    val pageSize: Int
) {

    data class FullJobs(
        val category: Category,
        val created_at: String,
        val employer_logo: String,
        val employer_name: String,
        val id: Int,
        val location: String,
        val max_salary: Int,
        val min_salary: Int,
        val nearest: String,
        val salary: String,
        val salary_type: String,
        val saved: Boolean,
        val title: String,
        val topping: Int
    ) {

        data class Category(
            val id: Int,
            val name: String
        )

    }

}



