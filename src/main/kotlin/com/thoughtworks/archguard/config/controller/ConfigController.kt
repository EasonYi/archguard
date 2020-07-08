package com.thoughtworks.archguard.config.controller

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.config.domain.NodeConfigure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/configures")
class ConfigController {
    @Autowired
    private lateinit var service: ConfigureService

    @GetMapping
    fun getConfigures() :List<NodeConfigure> {
        return service.getConfigures()
    }

    @PostMapping
    fun create(@RequestBody config: NodeConfigure): ResponseEntity<Nothing> {
        service.create(config)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody config: NodeConfigure): ResponseEntity<Nothing> {
        service.update(id, config)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Nothing> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}