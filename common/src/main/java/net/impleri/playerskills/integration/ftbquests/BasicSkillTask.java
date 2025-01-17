package net.impleri.playerskills.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.BooleanTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.impleri.playerskills.server.ServerApi;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.impleri.playerskills.variant.basic.BasicSkillType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class BasicSkillTask extends BooleanTask {
    public ResourceLocation skill;

    public BasicSkillTask(Quest quest) {
        super(quest);
    }

    protected ResourceLocation getSkillType() {
        return BasicSkillType.name;
    }

    @Override
    public TaskType getType() {
        return SkillTaskTypes.BASIC_SKILL;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("skill", skill.toString());
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        skill = SkillResourceLocation.of(nbt.getString("skill"));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(skill.toString(), Short.MAX_VALUE);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        skill = SkillResourceLocation.of(buffer.readUtf(Short.MAX_VALUE));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void getConfig(ConfigGroup config) {
        super.getConfig(config);

        List<ResourceLocation> skills = PlayerSkillsIntegration.getSkills(getSkillType());

        var firstSkill = skills.get(0);
        if (skill == null) {
            skill = firstSkill;
        }

        config.addEnum(
                "skill",
                skill,
                v -> skill = v,
                NameMap.of(firstSkill, skills).create(),
                firstSkill
        ).setNameKey("playerskills.quests.ui.skill");
    }

    @Override
    @Environment(EnvType.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("playerskills.quests.ui.skill").append(": ").append(Component.literal(skill.toString()).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return ServerApi.can(player, skill);
    }
}
