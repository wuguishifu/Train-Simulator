package com.bramerlabs.train_simulator.world;

import com.bramerlabs.engine.math.vector.Vector2f;
import com.bramerlabs.train_simulator.player.Player;
import com.bramerlabs.train_simulator.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    public HashMap<Chunk.Key, Chunk> loadedChunks;
    public HashMap<Chunk.Key, Chunk> visibleChunks;

    private final int seed;

    public World(int seed) {
        this.seed = seed;
        loadedChunks = new HashMap<>();
        visibleChunks = new HashMap<>();
    }

    public Chunk loadChunk(int x, int y) {
        return Chunk.generateChunk(x, y, seed);
    }

    public void setVisible(Chunk.Key chunkPos) {
        if (!loadedChunks.containsKey(chunkPos)) {
            loadedChunks.put(chunkPos, Chunk.generateChunk(chunkPos.x, chunkPos.y, seed));
        }
        visibleChunks.put(chunkPos, loadedChunks.get(chunkPos));
    }

    public void setInvisible(Chunk.Key chunkPos) {
        this.visibleChunks.remove(chunkPos);
    }

    public void unloadChunk(Chunk.Key chunkPos) {
        this.loadedChunks.remove(chunkPos);
    }

    public void update(Player player) {
        Chunk.Key inChunk = new Chunk.Key((int) (player.getPosition().x / (Chunk.SIZE * Chunk.TILE_SIZE)),
                (int) (player.getPosition().y / (Chunk.SIZE * Chunk.TILE_SIZE)));
        visibleChunks.clear();
        for (int x = -3; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                setVisible(new Chunk.Key(inChunk.x + x, inChunk.y + y));
            }
        }

        ArrayList<Chunk.Key> keysToUnload = new ArrayList<>();
        for (Chunk.Key key : loadedChunks.keySet()) {
            if (Math.abs(key.x - inChunk.x) > 5 || Math.abs(key.y - inChunk.y) > 5) {
                keysToUnload.add(key);
            }
        }
        for (Chunk.Key key : keysToUnload) {
            unloadChunk(key);
        }
    }

    public int getSeed() {
        return this.seed;
    }
}