//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wurmonline.cantorsdust.mods;


import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.*;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;

public class UnlimitedFatigue implements WurmMod, Configurable, PreInitable {
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    private boolean _unlimitedFatigueOn = true;

    public UnlimitedFatigue() {
    }

    public void configure(Properties properties) {
        this._unlimitedFatigueOn = Boolean.valueOf(properties.getProperty("unlimitedFatigueOn", Boolean.toString(this._unlimitedFatigueOn))).booleanValue();
        this.Log("Unlimited Fatigue On: ", this._unlimitedFatigueOn);
    }

    private void Log(String forFeature, boolean activated) {
        this._logger.log(Level.INFO, forFeature + activated);
    }

    public void preInit() {
        if(this._unlimitedFatigueOn) {
            this.UnlimitedFatigueFunction();
        }
    }

    private void UnlimitedFatigueFunction() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            CtMethod method = ex.getMethod("modifyStamina", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
            MethodInfo methodInfo = method.getMethodInfo();
            method.insertBefore("if ($1 < 0.0f) {\n" +
                    "            $1 = $1 * -1.0f;\n" +
                    "        }");
            //method.setBody("return true;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
    }

}
