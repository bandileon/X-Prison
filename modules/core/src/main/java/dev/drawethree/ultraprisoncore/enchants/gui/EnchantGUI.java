package dev.drawethree.ultraprisoncore.enchants.gui;

import dev.drawethree.ultraprisoncore.UltraPrisonCore;
import dev.drawethree.ultraprisoncore.enchants.UltraPrisonEnchants;
import dev.drawethree.ultraprisoncore.enchants.enchants.UltraPrisonEnchantment;
import dev.drawethree.ultraprisoncore.utils.compat.CompMaterial;
import dev.drawethree.ultraprisoncore.utils.item.ItemStackBuilder;
import dev.drawethree.ultraprisoncore.utils.misc.SkullUtils;
import dev.drawethree.ultraprisoncore.utils.text.TextUtils;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantGUI extends Gui {


	private static String GUI_TITLE;
	private static Item EMPTY_SLOT_ITEM;

	private static int PICKAXE_ITEM_SLOT;
	private static int HELP_ITEM_SLOT;
	private static int DISENCHANT_ITEM_SLOT;

	private static int GUI_LINES;

	private static Item HELP_ITEM;
	private static ItemStack DISENCHANT_ITEM;

	private static boolean PICKAXE_ITEM_ENABLED;
	private static boolean HELP_ITEM_ENABLED;
	private static boolean DISENCHANT_ITEM_ENABLED;

	static {
		reload();
	}

	@Getter
	@Setter
	private ItemStack pickAxe;

	@Getter
	private final int pickaxePlayerInventorySlot;

	public EnchantGUI(Player player, ItemStack pickAxe, int pickaxePlayerInventorySlot) {
		super(player, GUI_LINES, GUI_TITLE);

		this.pickAxe = pickAxe;
		this.pickaxePlayerInventorySlot = pickaxePlayerInventorySlot;

		Events.subscribe(InventoryCloseEvent.class, EventPriority.LOWEST)
				.filter(e -> e.getInventory().equals(this.getHandle()))
				.handler(e -> {
					UltraPrisonCore.getInstance().getEnchants().getEnchantsManager().handlePickaxeUnequip(this.getPlayer(), this.pickAxe);
					UltraPrisonCore.getInstance().getEnchants().getEnchantsManager().handlePickaxeEquip(this.getPlayer(), this.pickAxe);
				}).bindWith(this);

		// Checking for duping
		Schedulers.sync().runLater(() -> {
			if (!pickAxe.equals(this.getPlayer().getInventory().getItem(this.pickaxePlayerInventorySlot))) {
				this.close();
			}
		},10);
	}

	public static void reload() {

		GUI_TITLE = TextUtils.applyColor(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.title"));
		EMPTY_SLOT_ITEM = ItemStackBuilder.
				of(CompMaterial.fromString(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.empty_slots")).toItem()).buildItem().build();
		GUI_LINES = UltraPrisonEnchants.getInstance().getConfig().get().getInt("enchant_menu.lines");

		HELP_ITEM_ENABLED = UltraPrisonEnchants.getInstance().getConfig().get().getBoolean("enchant_menu.help_item.enabled", true);
		PICKAXE_ITEM_ENABLED = UltraPrisonEnchants.getInstance().getConfig().get().getBoolean("enchant_menu.pickaxe_enabled", true);
		DISENCHANT_ITEM_ENABLED = UltraPrisonEnchants.getInstance().getConfig().get().getBoolean("enchant_menu.disenchant_item.enabled", true);

		if (DISENCHANT_ITEM_ENABLED) {
			String base64 = UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.disenchant_item.Base64", null);

			if (base64 != null) {
				DISENCHANT_ITEM = ItemStackBuilder.of(SkullUtils.getCustomTextureHead(base64))
						.name(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.disenchant_item.name")).lore(UltraPrisonEnchants.getInstance().getConfig().get().getStringList("enchant_menu.disenchant_item.lore")).build();
			} else {
				DISENCHANT_ITEM = ItemStackBuilder.of(CompMaterial.fromString(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.disenchant_item.material")).toMaterial())
						.name(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.disenchant_item.name")).lore(UltraPrisonEnchants.getInstance().getConfig().get().getStringList("enchant_menu.disenchant_item.lore")).build();
			}
			DISENCHANT_ITEM_SLOT = UltraPrisonEnchants.getInstance().getConfig().get().getInt("enchant_menu.disenchant_item.slot");

		}

		if (HELP_ITEM_ENABLED) {
			String base64 = UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.help_item.Base64", null);

			if (base64 != null) {
				HELP_ITEM = ItemStackBuilder.of(SkullUtils.getCustomTextureHead(base64))
						.name(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.help_item.name")).lore(UltraPrisonEnchants.getInstance().getConfig().get().getStringList("enchant_menu.help_item.lore")).buildItem().build();
			} else {
				HELP_ITEM = ItemStackBuilder.of(CompMaterial.fromString(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.help_item.material")).toMaterial())
						.name(UltraPrisonEnchants.getInstance().getConfig().get().getString("enchant_menu.help_item.name")).lore(UltraPrisonEnchants.getInstance().getConfig().get().getStringList("enchant_menu.help_item.lore")).buildItem().build();
			}

			HELP_ITEM_SLOT = UltraPrisonEnchants.getInstance().getConfig().get().getInt("enchant_menu.help_item.slot");

		}

		if (PICKAXE_ITEM_ENABLED) {
			PICKAXE_ITEM_SLOT = UltraPrisonEnchants.getInstance().getConfig().get().getInt("enchant_menu.pickaxe_slot");
		}
	}

	@Override
	public void redraw() {

		// perform initial setup.
		if (isFirstDraw()) {
			for (int i = 0; i < this.getHandle().getSize(); i++) {
				this.setItem(i, EMPTY_SLOT_ITEM);
			}
		}

		if (HELP_ITEM_ENABLED) {
			this.setItem(HELP_ITEM_SLOT, HELP_ITEM);
		}

		if (DISENCHANT_ITEM_ENABLED) {
			this.setItem(DISENCHANT_ITEM_SLOT, ItemStackBuilder.of(DISENCHANT_ITEM).build(() -> {
				this.close();
				new DisenchantGUI(this.getPlayer(), this.pickAxe, this.pickaxePlayerInventorySlot).open();
			}));
		}

		if (PICKAXE_ITEM_ENABLED) {
			this.setItem(PICKAXE_ITEM_SLOT, Item.builder(this.pickAxe).build());
		}

		for (UltraPrisonEnchantment enchantment : UltraPrisonEnchantment.all()) {
			if (!enchantment.isEnabled()) {
				continue;
			}
			int level = UltraPrisonEnchants.getInstance().getEnchantsManager().getEnchantLevel(this.pickAxe, enchantment.getId());
			this.setItem(enchantment.getGuiSlot(), UltraPrisonEnchants.getInstance().getEnchantsManager().getGuiItem(enchantment, this, level));
		}
	}
}