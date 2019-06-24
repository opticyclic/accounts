package net.corda.accounts.contracts.states

import net.corda.accounts.contracts.AccountInfoContract
import net.corda.accounts.contracts.internal.schemas.AccountSchema
import net.corda.accounts.contracts.internal.schemas.PersistentAccountInfo
import net.corda.accounts.contracts.types.AccountStatus
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * A state which records the account name and number of an account as well as the node where it is currently hosted.
 *
 * @property name a string name for the account which will be unique at the level of the account host
 * @property host a Corda node, specified by a [Party] which hosts the account
 * @property id an UUID which should be unique at the network level
 * @property status indicates whether the account is open/closed/etc.
 */
@BelongsToContract(AccountInfoContract::class)
data class AccountInfo(
        val name: String,
        val host: Party,
        val id: UniqueIdentifier,
        val status: AccountStatus = AccountStatus.ACTIVE
) : LinearState, QueryableState {

    override val linearId: UniqueIdentifier get() = id

    override val participants: List<AbstractParty> get() = listOf(host)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        if (schema is AccountSchema) {
            return PersistentAccountInfo(
                    name = name,
                    host = host,
                    id = id.id,
                    status = status
            )
        } else {
            throw IllegalStateException("Cannot construct instance of ${this.javaClass} from Schema: $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        return listOf(AccountSchema)
    }
}





