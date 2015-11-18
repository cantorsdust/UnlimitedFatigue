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
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;

public class UnlimitedStamina implements WurmMod, Configurable, PreInitable {
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    private boolean _unlimitedStaminaOn = true;
    private boolean _unlimitedStaminaMovingOn = true;

    public UnlimitedStamina() {
    }

    public void configure(Properties properties) {
        this._unlimitedStaminaOn = Boolean.valueOf(properties.getProperty("unlimitedStaminaOn", Boolean.toString(this._unlimitedStaminaOn))).booleanValue();
        this.Log("Unlimited Stamina On: ", this._unlimitedStaminaOn);
        this._unlimitedStaminaMovingOn = Boolean.valueOf(properties.getProperty("unlimitedStaminaMovingOn", Boolean.toString(this._unlimitedStaminaMovingOn))).booleanValue();
        this.Log("Unlimited Moving Stamina On: ", this._unlimitedStaminaMovingOn);
    }

    private void Log(String forFeature, boolean activated) {
        this._logger.log(Level.INFO, forFeature + activated);
    }

    public void preInit() {
        if(this._unlimitedStaminaOn) {
            this.UnlimitedStaminaFunction();
        }

        else {
            if(this._unlimitedStaminaMovingOn) {
                this.UnlimitedStaminaMovingFunction();
            }
        }

    }

    private void UnlimitedStaminaFunction() {
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


    private void UnlimitedStaminaMovingFunction() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass e = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.MovementScheme");
            CtMethod move = e.getDeclaredMethod("move");
            move.instrument(new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    //if(methodCall.getClassName().equals("com.wurmonline.server.creatures.MovementScheme") && methodCall.getMethodName().equals("modifyStamina")) {
                    if(methodCall.getMethodName().equals("modifyStamina")) {
                        String replaceString = "\n";
                        methodCall.replace(replaceString);
                    }
                }
            });
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
    }

}
