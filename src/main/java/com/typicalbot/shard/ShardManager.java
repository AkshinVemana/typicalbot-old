/**
 * TypicalBot - A multipurpose discord bot
 * Copyright (C) 2016-2018 Bryan Pikaard & Nicholas Sylke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.typicalbot.shard;

import com.google.common.primitives.Ints;

import javax.security.auth.login.LoginException;

public class ShardManager {
    /**
     * The maximum shards available in current session.
     */
    private static int MAX_SHARDS;

    /**
     * A collection of shard instances.
     */
    private static Shard[] shards;

    /**
     * Register JDA instances.
     *
     * @param shardCount The amount of shards to be registered.
     * @throws LoginException
     * @throws InterruptedException
     */
    public static void register(int shardCount) throws LoginException, InterruptedException {
        // This should eventually be changed to an option in the configuration
        // and allow for on-demand shard addition/deletion.
        MAX_SHARDS = shardCount;

        shards = new Shard[MAX_SHARDS];

        for (int i = 0; i < shardCount; i++) {
            Shard shard = new Shard(i, shardCount);
            shards[i] = shard;

            // Clients are limited to 1 identity every 5 seconds.
            Thread.sleep(5000);
        }
    }

    /**
     * Get the shard of a specific guild ID.
     *
     * @param guildId The guild ID.
     * @return The @{@link Shard} in accordance to the guild.
     */
    public static Shard getShard(long guildId) {
        // The sharding formula from Discord developer documentation.
        long shardId = (guildId >> 22) % MAX_SHARDS;

        return getShard(Ints.checkedCast(shardId));
    }

    /**
     * Get the shard of a specific shard ID.
     *
     * @param shardId The shard ID.
     * @return The {@link Shard} of the specific ID.
     */
    public static Shard getShard(int shardId) {
        return shards[shardId];
    }

    /**
     * Restart a specific shard from an ID.
     *
     * @param shardId The shard ID.
     * @throws LoginException
     */
    public static void restart(int shardId) throws LoginException {
        Shard shard = getShard(shardId);

        // Shut down the shard through JDA.
        shard.getInstance().shutdown();

        // Create a new instance of the specific shard.
        shard = new Shard(shardId, MAX_SHARDS);

        // Update the shard instance in the shard collection.
        shards[shardId] = shard;
    }

    /**
     * Get all shards.
     *
     * @return The shard collection.
     */
    public static Shard[] getShards() {
        return shards;
    }
}
