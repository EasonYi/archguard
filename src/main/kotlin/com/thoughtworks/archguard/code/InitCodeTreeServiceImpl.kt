package com.thoughtworks.archguard.code

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class InitCodeTreeServiceImpl(val jClassRepository: JClassRepository) : InitCodeTreeService {
    override fun initCodeTree(systemId: Long): CodeTree {
        val codeTree = CodeTree()
        jClassRepository.getAllBySystemId(systemId).filter { it.module != null }.map { codeTree.addClass(it.getFullName()) }
        codeTree.fixTopNodeSubModuleType()
        return codeTree
    }

}
