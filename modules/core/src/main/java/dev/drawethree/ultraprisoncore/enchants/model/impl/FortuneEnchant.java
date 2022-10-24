package dev.drawethree.ultraprisoncore.enchants.model.impl;

import dev.drawethree.ultraprisoncore.enchants.UltraPrisonEnchants;
import dev.drawethree.ultraprisoncore.enchants.model.UltraPrisonEnchantment;
import dev.drawethree.ultraprisoncore.utils.compat.CompMaterial;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FortuneEnchant extends UltraPrisonEnchantment {

	private List<CompMaterial> whiteListedBlocks;

	public FortuneEnchant(UltraPrisonEnchants instance) {
		super(instance, 3);
		this.whiteListedBlocks = plugin.getEnchantsConfig().getYamlConfig().getStringList("enchants." + id + ".Whitelist").stream().map(CompMaterial::fromString).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public void onEquip(Player p, ItemStack pickAxe, int level) {
		ItemMeta meta = pickAxe.getItemMeta();
		meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
		pickAxe.setItemMeta(meta);
	}

	@Override
	public void onUnequip(Player p, ItemStack pickAxe, int level) {

	}

	@Override
	public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {

	}

	@Override
	public void reload() {
		super.reload();
		this.whiteListedBlocks = plugin.getEnchantsConfig().getYamlConfig().getStringList("enchants." + id + ".Whitelist").stream().map(CompMaterial::fromString).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public String getAuthor() {
		return "Drawethree";
	}


	public boolean isBlockWhitelisted(Block block) {
		CompMaterial blockMaterial = CompMaterial.fromBlock(block);
		return this.whiteListedBlocks.contains(blockMaterial);
	}
}