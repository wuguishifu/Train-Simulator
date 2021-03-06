package com.bramerlabs.train_simulator.world.chunk;

import com.bramerlabs.engine.math.noise.ImprovedNoise;
import com.bramerlabs.engine.math.vector.Vector2f;
import com.bramerlabs.engine.math.vector.Vector3f;
import com.bramerlabs.train_simulator.world.title.Tile;

import java.util.Objects;

public class Chunk {

    private final Tile[][] tiles;

    public static final int SIZE = 16;
    public static final float TILE_SIZE = 0.5f;

    private final Vector2f position;

    public Chunk(Vector2f position, Tile[][] tiles) {
        this.tiles = tiles;
        this.position = position;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= SIZE || x < 0 || y >= SIZE || y < 0) {
            return;
        }
        tiles[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        if (x >= SIZE || x < 0 || y >= SIZE || y < 0) {
            return null;
        }
        return tiles[x][y];
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public static Tile[][] generateTiles(int chunkX, int chunkY, int seed) {
        Tile[][] tiles = new Tile[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                float seedSize = 10.f;
                float sampleX = (chunkX * SIZE + x) / seedSize;
                float sampleY = (chunkY * SIZE + y) / seedSize;
                double noise = ImprovedNoise.noise(sampleX, sampleY, seed) + 0.5;
                int type;
                if (noise < 0.3f) {
                    type = 0;
                } else if (noise < 0.5f) {
                    type = 1;
                } else if (noise < 0.6f) {
                    type = 2;
                } else if (noise < 0.85f) {
                    type = 3;
                } else if (noise < 0.9f){
                    type = 4;
                } else {
                    type = 4;
                }
//                System.out.println((new Vector3f(chunkX, chunkY, seed)) + ", " +
//                        new Vector2f(chunkX * SIZE + x, chunkY * SIZE + y) + ", " + noise + ", " + type);
                System.out.println(noise);
                tiles[x][y] = new Tile(type);
            }
        }
        return tiles;
    }

    public static Chunk generateChunk(int chunkX, int chunkY, int seed) {
        Tile[][] tiles = generateTiles(chunkX, chunkY, seed);
        return new Chunk(new Vector2f(chunkX, chunkY), tiles);
    }

    public static class Key {

        public final int x, y;

        public Key(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return x == key.x && y == key.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
