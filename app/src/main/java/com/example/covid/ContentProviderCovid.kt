package pt.ipg.livros

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.example.covid.BdCovidOpenHelper
import com.example.covid.TabelaInfetados
import com.example.covid.TabelaPacientes
import com.example.covid.TabelaVacinados
import kotlin.math.E

class ContentProviderLivros : ContentProvider() {
    private var bdLivrosOpenHelper: BdCovidOpenHelper? = null

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     *
     *
     * You should defer nontrivial initialization (such as opening,
     * upgrading, and scanning databases) until the content provider is used
     * (via [.query], [.insert], etc).  Deferred initialization
     * keeps application startup fast, avoids unnecessary work if the provider
     * turns out not to be needed, and stops database errors (such as a full
     * disk) from halting application launch.
     *
     *
     * If you use SQLite, [android.database.sqlite.SQLiteOpenHelper]
     * is a helpful utility class that makes it easy to manage databases,
     * and will automatically defer opening until first use.  If you do use
     * SQLiteOpenHelper, make sure to avoid calling
     * [android.database.sqlite.SQLiteOpenHelper.getReadableDatabase] or
     * [android.database.sqlite.SQLiteOpenHelper.getWritableDatabase]
     * from this method.  (Instead, override
     * [android.database.sqlite.SQLiteOpenHelper.onOpen] to initialize the
     * database when it is first opened.)
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    override fun onCreate(): Boolean {
        bdLivrosOpenHelper = BdCovidOpenHelper(context)

        return true
    }

    /**
     * Implement this to handle query requests from clients.
     *
     *
     * Apps targeting [android.os.Build.VERSION_CODES.O] or higher should override
     * [.query] and provide a stub
     * implementation of this method.
     *
     *
     * This method can be called from multiple threads, as described in
     * [Processes
 * and Threads]({@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads).
     *
     *
     * Example client call:
     *
     *
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:
     *
     *
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     *
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     *
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     *
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri The URI to query. This will be the full URI sent by the client;
     * if the client is requesting a specific record, the URI will end in a record number
     * that the implementation should parse and add to a WHERE or HAVING clause, specifying
     * that _id value.
     * @param projection The list of columns to put into the cursor. If
     * `null` all columns are included.
     * @param selection A selection criteria to apply when filtering rows.
     * If `null` then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     * the values from selectionArgs, in order that they appear in the selection.
     * The values will be bound as Strings.
     * @param sortOrder How the rows in the cursor should be sorted.
     * If `null` then the provider is free to define the sort order.
     * @return a Cursor or `null`.
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val bd = bdLivrosOpenHelper!!.readableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_PACIENTES -> TabelaPacientes(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_PACIENTE_ESPECIFICO -> TabelaPacientes(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            URI_INFETADOS -> TabelaInfetados(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_INFETADO_ESPECIFICO -> TabelaInfetados(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )
            URI_VACINADOS -> TabelaVacinados(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>?,
                null,
                null,
                sortOrder
            )

            URI_VACINADO_ESPECIFICO -> TabelaVacinados(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!),
                null,
                null,
                null
            )

            else -> null
        }
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * `vnd.android.cursor.item` for a single record,
     * or `vnd.android.cursor.dir/` for multiple items.
     * This method can be called from multiple threads, as described in
     * [Processes
 * and Threads]({@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads).
     *
     *
     * Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or `null` if there is no type.
     */
    override fun getType(uri: Uri): String? {
        return when (getUriMatcher().match(uri)) {
            URI_PACIENTES -> "$MULTIPLOS_ITEMS/$PACIENTES"
            URI_PACIENTE_ESPECIFICO -> "$UNICO_ITEM/$PACIENTES"
            URI_INFETADOS -> "$MULTIPLOS_ITEMS/$INFETADOS"
            URI_INFETADO_ESPECIFICO -> "$UNICO_ITEM/$INFETADOS"
            URI_VACINADOS -> "$MULTIPLOS_ITEMS/$VACINADOS"
            URI_VACINADO_ESPECIFICO -> "$UNICO_ITEM/$VACINADOS"
            else -> null
        }
    }


    /**
     * Implement this to handle requests to insert a new row. As a courtesy,
     * call
     * [ notifyChange()][ContentResolver.notifyChange] after inserting. This method can be called from multiple
     * threads, as described in [Processes
 * and Threads](
      {@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads).
     *
     * @param uri The content:// URI of the insertion request.
     * @param values A set of column_name/value pairs to add to the database.
     * @return The URI for the newly inserted item.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        val id = when (getUriMatcher().match(uri)) {
            URI_PACIENTES -> TabelaPacientes(bd).insert(values!!)
            URI_INFETADOS -> TabelaInfetados(bd).insert(values!!)
            URI_VACINADOS -> TabelaInfetados(bd).insert(values!!)

            else -> -1L
        }

        if (id == -1L) return null

        return Uri.withAppendedPath(uri, id.toString())
    }

    /**
     * Implement this to handle requests to delete one or more rows. The
     * implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     * As a courtesy, call
     * [ notifyChange()][ContentResolver.notifyChange] after deleting. This method can be called from multiple
     * threads, as described in [Processes
 * and Threads](
      {@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads).
     *
     *
     * The implementation is responsible for parsing out a row ID at the end of
     * the URI, if a specific row is being deleted. That is, the client would
     * pass in `content://contacts/people/22` and the implementation
     * is responsible for parsing the record number (22) when creating a SQL
     * statement.
     *
     * @param uri The full URI to query, including a row ID (if a specific
     * record is requested).
     * @param selection An optional restriction to apply to rows when deleting.
     * @return The number of rows affected.
     * @throws SQLException
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_PACIENTE_ESPECIFICO -> TabelaPacientes(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_INFETADO_ESPECIFICO -> TabelaInfetados(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )
            URI_VACINADO_ESPECIFICO -> TabelaInfetados(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> 0
        }
    }

    /**
     * Implement this to handle requests to update one or more rows. The
     * implementation should update all rows matching the selection to set the
     * columns according to the provided values map. As a courtesy, call
     * [ notifyChange()][ContentResolver.notifyChange] after updating. This method can be called from multiple
     * threads, as described in [Processes
 * and Threads](
      {@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads).
     *
     * @param uri The URI to query. This can potentially have a record ID if
     * this is an update request for a specific record.
     * @param values A set of column_name/value pairs to update in the database.
     * @param selection An optional filter to match rows to update.
     * @return the number of rows affected.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val bd = bdLivrosOpenHelper!!.writableDatabase

        return when (getUriMatcher().match(uri)) {
            URI_PACIENTE_ESPECIFICO -> TabelaPacientes(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            URI_INFETADO_ESPECIFICO -> TabelaInfetados(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )
            URI_VACINADO_ESPECIFICO -> TabelaInfetados(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> 0
        }
    }

    companion object {
        private const val AUTHORITY = "com.example.covid"

        private const val PACIENTES = "pacientes"
        private const val INFETADOS = "infetados"
        private const val VACINADOS = "vacinados"

        private const val URI_PACIENTES = 100
        private const val URI_PACIENTE_ESPECIFICO = 101
        private const val URI_INFETADOS = 200
        private const val URI_INFETADO_ESPECIFICO = 201
        private const val URI_VACINADOS = 300
        private const val URI_VACINADO_ESPECIFICO = 301


        private const val MULTIPLOS_ITEMS = "vnd.android.cursor.dir"
        private const val UNICO_ITEM = "vnd.android.cursor.item"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        public val ENDERECO_PACIENTES = Uri.withAppendedPath(ENDERECO_BASE, PACIENTES)
        public val ENDERECO_INFETADOS = Uri.withAppendedPath(ENDERECO_BASE, INFETADOS)
        public val ENDERECO_VACINADOS = Uri.withAppendedPath(ENDERECO_BASE, VACINADOS)

        private fun getUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(AUTHORITY, PACIENTES, URI_PACIENTES)
            uriMatcher.addURI(AUTHORITY, "$PACIENTES/#", URI_PACIENTE_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, INFETADOS, URI_INFETADOS)
            uriMatcher.addURI(AUTHORITY, "$INFETADOS/#", URI_INFETADO_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, VACINADOS, URI_VACINADOS)
            uriMatcher.addURI(AUTHORITY, "$VACINADOS/#", URI_VACINADO_ESPECIFICO)

            return uriMatcher
        }
    }
}
