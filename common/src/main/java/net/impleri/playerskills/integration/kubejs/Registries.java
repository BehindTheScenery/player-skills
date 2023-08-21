package net.impleri.playerskills.integration.kubejs;

//import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.impleri.playerskills.api.Skill;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class Registries {
    private static final ResourceKey<Registry<Skill<?>>> key = ResourceKey.createRegistryKey(SkillResourceLocation.of("skill_builders_registry"));

//    public static final RegistryObjectBuilderTypes<Skill<?>> SKILLS = RegistryObjectBuilderTypes.add(key, Skill.class);
//    public static final RegistryObjectBuilderTypes<Skill<?>> SKILLS = RegistryObjectBuilderTypes.add(key, Skill.class);
    public static final RegistryInfo SKILLS = RegistryInfo.of(key).type(Skill.class);
}
