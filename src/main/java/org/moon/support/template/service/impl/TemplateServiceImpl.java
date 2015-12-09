package org.moon.support.template.service.impl;

import org.moon.base.service.AbstractService;
import org.moon.support.template.domain.Template;
import org.moon.support.template.repository.TemplateRepository;
import org.moon.support.template.service.TemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:Gavin
 * @date 2015/6/16 0016
 */
@Service
public class TemplateServiceImpl extends AbstractService implements TemplateService {

    @Resource
    private TemplateRepository templateRepository;

    @Override
    public Template save(Template template) {
        templateRepository.save(template);
        return template;
    }

    @Override
    public Template update(Template template) {
        templateRepository.update(template);
        return template;
    }

    @Override
    public boolean delete(Long id) {
        return templateRepository.delete(id) > 0;
    }

    @Override
    public boolean delete(Template template) {
        return templateRepository.delete(template.getId()) > 0;
    }

    @Override
    public Template get(Long id) {
        return templateRepository.get(id);
    }
}
