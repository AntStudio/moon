
/**
 * 判断是否为空
 */
Handlebars.registerHelper('nonEmpty', function(variable, options) {
    if(variable && variable.length > 0) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

/**
 * 判断是否相等
 */
Handlebars.registerHelper('eq', function(var1,var2,options) {
    console.log(var1 +"..."+var2);
    if(var1 == var2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

/**
 * 判断是否全等
 */
Handlebars.registerHelper('congruent', function(var1,var2,options) {
    if(var1 === var2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

/**
 * 判断和系统变量是否相等
 */
Handlebars.registerHelper('eqConstant', function(var1,var2, options) {
    if(var1 == moon.constants[var2]) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});