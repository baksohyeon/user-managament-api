package io.dhlottery.user.api.user_management_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["io.dhlottery.user"])
@EnableJpaRepositories(basePackages = ["io.dhlottery.user.repository"])
@EntityScan(basePackages = ["io.dhlottery.user.entity"])
class UserManagementApiApplication

fun main(args: Array<String>) {
	runApplication<UserManagementApiApplication>(*args)
}
