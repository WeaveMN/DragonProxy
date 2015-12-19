/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

public class PEInventorySlot {

    public short id;
    public byte count;
    public short meta;
    public CompoundTag nbt;

    public PEInventorySlot() {
        this((short) 0, (byte) 0, (short) 0);
    }

    public PEInventorySlot(short id, byte count, short meta) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        nbt = new CompoundTag("");
    }

    public PEInventorySlot(short id, byte count, short meta, CompoundTag nbt) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        this.nbt = nbt;
    }
    
    

    public static PEInventorySlot readSlot(PEBinaryReader reader) throws IOException {
        short id = (short)(reader.readShort() & 0xFFFF); //Unsigned
        if(id <= 0){
            return new PEInventorySlot((short)0, (byte)0, (short)0);
        }
        byte count = reader.readByte();
        short meta = reader.readShort();
        short lNbt = reader.readShort();
        if(lNbt <= 0){
            return new PEInventorySlot(id, count, meta);
        }
        byte[] nbtData = reader.read(lNbt);
        CompoundTag nbt = (CompoundTag) NBTIO.readTag(new DataInputStream(new ByteArrayInputStream(nbtData)));
        return new PEInventorySlot(id, count, meta, nbt);
    }

    public static void writeSlot(PEBinaryWriter writer, PEInventorySlot slot) throws IOException {
        if(slot == null || (slot != null && slot.id == 0)){
            writer.writeShort((short)0);
            return;
        }
        writer.writeShort(slot.id);
        writer.writeByte(slot.count);
        writer.writeShort(slot.meta);
        if(slot.nbt == null){
            writer.writeShort((short) 0);
        }else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            NBTIO.writeTag(new DataOutputStream(bos), slot.nbt);
            byte[] nbtdata = bos.toByteArray();
            writer.writeShort((short)(nbtdata.length & 0xFFFF));
            writer.write(nbtdata);
        }
    }
    
    public static PEInventorySlot fromItemStack(ItemStack item){
        PEInventorySlot slot = new PEInventorySlot();
        slot.id = (short)(item.getId() & 0xFFFF);
        if(slot.id <= 0){
            return slot;
        }
        slot.count = (byte)(item.getAmount() & 0xFF);
        slot.meta = (short)(item.getData() & 0xFFFF);
        slot.nbt = item.getNBT();
        return slot;
    }

    @Override
    public String toString() {
        return "{PE Item: ID=" + this.id + ", Count=" + (this.count & 0xFF) + ", Data=" + this.meta + "}";
    }
}
